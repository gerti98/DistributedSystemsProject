%%%-------------------------------------------------------------------
%%% @author fraie
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 08. feb 2022 11:06
%%%-------------------------------------------------------------------
-module(my_supervisor).
-author("fraie").
-behaviour(supervisor).

%% API exports
-export([start_link/0, add_auction_to_monitor/3]).
-export([init/1]).

add_auction_to_monitor(AuctionPid, AuctionName, Duration) ->
  AuctionChildSpec = #{id => AuctionPid,
    start => {AuctionPid, init_auction_handler, [AuctionName, Duration]},
    restart => transient},
  %% Transient means that the child is restarted only if it terminates abnormally
  Result = supervisor:start_child(self(), AuctionChildSpec),
  io:format(" [SUPERVISOR] Result of adding dinamycally a child is ~p ~n", [Result]),
  Result.

start_link() ->
  Args = [],
  {State, PidSupervisor} = supervisor:start_link({local, ?MODULE}, ?MODULE, Args),
  io:format(" [SUPERVISOR] Supervisor pid is ~p state is ~p~n", [PidSupervisor, State]),
  register(auctions_supervisor, PidSupervisor),
  PidSupervisor.

%% The init callback function is called by the 'supervisor' module.
init(_Args) ->
  io:format(" [SUPERVISOR] Init function started ~n"),
  %% Configuration options common to all children.
  %% If a child process crashes, restart only that one (one_for_one).
  %% If there is more than 1 crash ('intensity') in
  %% 5 seconds ('period'), the entire supervisor crashes
  %% with all its children.
  SupFlags = #{strategy => one_for_one,
    intensity => 1,
    period => 5},

  MainServerPid = whereis(main_server_endpoint),
  io:format(" [SUPERVISOR] First child monitored is the main server whose pid is ~p~n",[MainServerPid]),

  ChildMainServer = #{id => MainServerPid,
    start => {MainServerPid, start_main_server, []},
    restart => permanent},
  %% permanent means that this process is always restarted
  %% There is only one child at the beginning
  Children = [ChildMainServer],

  %% Return the supervisor flags and the child specifications
  %% to the 'supervisor' module.
  {ok, {SupFlags, Children}}.