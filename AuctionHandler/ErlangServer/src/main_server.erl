%%%-------------------------------------------------------------------
%%% @author fraie
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 20. gen 2022 14:58
%%%-------------------------------------------------------------------
-module(main_server).
-author("fraie").

-behavior(gen_server).
-import(mnesia_db, [create_mnesia_db/0, start_mnesia/0, stop_mnesia_db/0, add_user/2, get_user/1]).

%% This server must be able to responds to client requests for:
%% - the list of online users
%% - the list of active auctions
%% - registering and create a new auction
%% - ecc.... (TODO)
%% API
-export([start_main_server/0, get_user_list/0, get_active_auction_list/0, create_new_auction/0, reset/0, get_user_by_username/1, register_user/2]).
-export([init/1, handle_call/3]). %% Necessary otherwise nothing work

% API functions

start_main_server() ->
  Return = gen_server:start({local, main_server}, ?MODULE, [], []),
  io:format("start_link: ~p~n", [Return]),
  Return.

get_user_list() ->
  gen_server:call(main_server, user_list).

get_user_by_username(Username) ->
  gen_server:call(main_server, {get_user, Username}).

register_user(Username, Pw) ->
  gen_server:call(main_server, {register, Username, Pw}).

get_active_auction_list() ->
  gen_server:call(main_server, auction_list).

create_new_auction() ->
  gen_server:call(main_server, new_auction).

reset() ->
  gen_server:cast(main_server, reset).


% gen_server CALLBACK FUNCTIONS
%% For now the state is {0,0} but it can be anything TODO
init([]) ->
  mnesia_db:start_mnesia(),
  {ok, {0,0}}.   % general format: {ok, InitialState}

handle_call(user_list, _From, {0, 0}) ->
  {reply, {todo, da, user_list_call}, {0,0}};
handle_call(auction_list, _From, {0, 0}) ->
  {reply, {todo, da, auction_list_call}, {0,0}};
handle_call(new_auction, _From, {0, 0}) ->
  {reply, {todo, da, new_auction_call}, {0,0}};
handle_call({get_user, Username}, _From, {0,0}) ->
  Ret = mnesia_db:get_user(Username),
  {reply, Ret, {0,0}};
handle_call({register, Username, Pw}, _From, {0,0}) ->
  Ret = mnesia_db:add_user(Username, Pw),
  {reply, Ret, {0,0}}.

%% Template Functions (from examples):
%% handle_call({gimme,N}, _From, {Total, Times}) ->
%%  NewTotal = Total+N,
%%  NewTimes = Times+1,
%%  {reply, NewTotal/NewTimes, {NewTotal, NewTimes}}; % general format: {reply, ReplyPayload, NewState}
%%
%% handle_cast(reset, _State) ->
%%  {noreply, {0,0}}.           % general format: {noreply, NewState}
