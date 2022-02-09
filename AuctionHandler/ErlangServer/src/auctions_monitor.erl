%%%-------------------------------------------------------------------
%%% @author fraie
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 09. feb 2022 14:22
%%%-------------------------------------------------------------------
-module(auctions_monitor).
-author("fraie").

%% API
-export([start_auctions_monitor/0]).

start_auctions_monitor() ->
  io:format(" [AUCTIONS MONITOR] Auctions Monitor started with pid ~p~n", [self()]),
  register(auctions_monitor, self()),
  auctions_monitor_loop([]).

auctions_monitor_loop(ProcessToMonitor) ->
  receive
    {add_auction_to_monitor, AuctionPid, AuctionName, Duration} ->
      io:format(" [AUCTIONS MONITOR] Received a request for monitoring ~p ~p~n", [AuctionPid, AuctionName]),
      Res = monitor(process, AuctionPid),
      io:format(" [AUCTIONS MONITOR] Ritorno monitor req ~p~n", [Res]),
      NewProcessToMonitor = ProcessToMonitor ++ [{AuctionPid, AuctionName, Duration}],
      io:format(" [AUCTIONS MONITOR] Process to monitor list ~p~n", [NewProcessToMonitor]),
      auctions_monitor_loop(NewProcessToMonitor);
    {'DOWN', MonitorRef, process, Pid, normal} ->
      io:format(" [AUCTIONS MONITOR] The process ~p is crashed with reason normal and with monitor ref ~p~n", [Pid, MonitorRef]),
      io:format(" [AUCTIONS MONITOR] DEBUG process list before delete ~p~n", [ProcessToMonitor]),
      Tuple = lists:keyfind(Pid, 1, ProcessToMonitor),
      io:format(" [AUCTIONS MONITOR] key find returns ~p~n", [Tuple]),
      NewProcessToMonitor = lists:delete(Tuple, ProcessToMonitor),
      io:format(" [AUCTIONS MONITOR] DEBUG process list after delete ~p~n", [NewProcessToMonitor]),
      auctions_monitor_loop(NewProcessToMonitor);
    {'DOWN', MonitorRef, process, Pid, Reason} ->
      io:format(" [AUCTIONS MONITOR] The process ~p is crashed with reason ~p and with monitor ref ~p~n", [Pid, Reason, MonitorRef]),
      Tuple = lists:keyfind(Pid, 1, ProcessToMonitor),
      io:format(" [AUCTIONS MONITOR] The process to respawn is ~p~n", [Tuple]),
      AuctionName = element(2,Tuple),
      AuctionDuration = element(3,Tuple),
      PidHandler = spawn( fun() -> auction_handler:init_auction_handler(AuctionName, AuctionDuration) end),
      io:format(" [AUCTIONS MONITOR] I have respawn the process ~p now its pid is ~p~n", [Tuple, PidHandler]),
      %% Ask the main_server to update the pid
      Endpoint = whereis(main_server_endpoint),
      Endpoint ! {update_pid, AuctionName, PidHandler},
      %% Delete the old crashed process from the list
      lists:delete(Tuple, ProcessToMonitor),
      %% I self-add the spawn process to myself (i.e. monitor)
      self() ! {add_auction_to_monitor, PidHandler, AuctionName, AuctionDuration},
      auctions_monitor_loop(ProcessToMonitor);
    _ ->  io:format(" [AUCTIONS MONITOR] Unexpected message ~n")
  end.
