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
  auctions_monitor_loop().

auctions_monitor_loop() ->
  receive
    {add_auction_to_monitor, AuctionPid} ->
      io:format(" [AUCTIONS MONITOR] Received a request for monitoring ~p~n", [AuctionPid]),
      Res = monitor(process, AuctionPid),
      io:format(" [AUCTIONS MONITOR] Ritorno monitor req ~p~n", [Res]),
      auctions_monitor_loop();
    {'DOWN', MonitorRef, process, Pid, normal} ->
      io:format(" [AUCTIONS MONITOR] The process ~p is crashed with reason normal and with monitor ref ~p~n", [Pid, MonitorRef]),
      auctions_monitor_loop();
    {'DOWN', MonitorRef, process, Pid, Reason} ->
      io:format(" [AUCTIONS MONITOR] The process ~p is crashed with reason ~p and with monitor ref ~p~n", [Pid, Reason, MonitorRef]),
      auctions_monitor_loop();
    _ ->  io:format(" [AUCTIONS MONITOR] Unexpected message ~n")
  end.
