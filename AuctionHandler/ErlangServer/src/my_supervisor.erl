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
-export([start_link/0]).
-export([init/1]).


start_link() ->
  Args = [],
  %% IMPORTANT: supervisor:start_link register globally the supervisor as "my_supervisor"
  {State, PidSupervisor} = supervisor:start_link({global, ?MODULE}, ?MODULE, Args),
  io:format(" [SUPERVISOR] Supervisor pid is ~p state is ~p~n", [PidSupervisor, State]),
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
  ChildMainServer = #{id => main_server,
    start => {main_server, start_main_server, []},
    restart => permanent},
  AuctionMonitor = #{id => auctions_monitor,
    start => {auctions_monitor, start_auctions_monitor, []},
    restart => permanent},
  %% permanent means that this process is always restarted
  Children = [ChildMainServer, AuctionMonitor],
  %% Return the supervisor flags and the child specifications
  %% to the 'supervisor' module.
  {ok, {SupFlags, Children}}.