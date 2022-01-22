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
-import(auction_handler, [auction_loop/0]).

%% This server must be able to responds to client requests for:
%% - the list of online users
%% - the list of active auctions
%% - registering and create a new auction
%% - ecc.... (TODO)
%% API
-export([start_main_server/0, get_user_list/0, get_active_auction_list/0, create_new_auction/3, reset/0, get_user_by_username/1, register_user/2, endpoint_loop/0, login_user/2]).
-export([init/1, handle_call/3, handle_cast/2]). %% Necessary otherwise nothing work

% API functions

start_main_server() ->
  mnesia_db:create_mnesia_db(),
  Return = gen_server:start({local, main_server}, ?MODULE, [], []),
  io:format("start_link: ~p~n", [Return]),
  Pid = spawn(?MODULE, endpoint_loop, []),
  register(main_server_endpoint, Pid),
  Return.

%% ---- IMPORTANT ---
%% Expected reply for correct operation (login, registration) from server: {self(), ok}
%% Expected reply for any error on operation from server: {self(), false}
%% I.E.
%% The client will send a message like this: {#Pid<client@localhost.1.0>,login,#{"username" => "Pippo","password" => "Pippo"}}
%% The server will reply with {self(), ok} if User "Pippo" exists and the related pwd is "Pippo", false otherwise
endpoint_loop() ->
  receive
    {ClientPid, register, MessageMap} ->
      io:format("Received a register message~n"),
      Result = register_user(maps:get("username", MessageMap), maps:get("password", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, login, MessageMap} ->
      io:format("Received a login message~n"),
      Result = login_user(maps:get("username", MessageMap), maps:get("password", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, create_auction, MessageMap} ->
      Result = create_new_auction(maps:get("goodName", MessageMap), maps:get("startingValue", MessageMap),maps:get("username", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, get_active_auctions, MessageMap} ->
      Result = get_active_auction_list(),
      ClientPid ! {self(), Result};
    _ -> io:format("Received any message~n")
  end,
  endpoint_loop().


get_user_list() ->
  gen_server:call(main_server, user_list).

get_user_by_username(Username) ->
  gen_server:call(main_server, {get_user, Username}).

register_user(Username, Pw) ->
  case gen_server:call(main_server, {register, Username, Pw}) of
    {atomic, ok} -> ok;
    _ -> false
  end.

login_user(Username, Pw) ->
  case gen_server:call(main_server, {get_user, Username}) of
    {atomic, [UserTuple | _]} ->
      %% The second field of UserTuple is the Password
      case lists:nth(2, UserTuple) == Pw of
        true -> ok;
        false -> false
      end;
    _ -> false
  end.


get_active_auction_list() ->
  gen_server:call(main_server, auction_list).

create_new_auction(ObjName, InitValue, Creator) ->
  gen_server:call(main_server, {new_auction, ObjName, InitValue, Creator}).

reset() ->
  gen_server:cast(main_server, reset).


% gen_server CALLBACK FUNCTIONS
%% The server state maintain the list of active auctions
init([]) ->
  mnesia_db:start_mnesia(),
  {ok, []}.   % general format: {ok, InitialState}

handle_call(user_list, _From, ServerState) ->
  {reply, {todo, da, user_list_call}, ServerState};
handle_call(auction_list, _From, ServerState) ->
  {reply, ServerState, ServerState};
handle_call({new_auction, ObjName, InitValue, Creator}, _From, ServerState) ->
  PidHandler = spawn( fun() -> auction_handler:auction_loop() end),
  Ret = mnesia_db:add_auction(ObjName, InitValue, Creator, PidHandler),
  io:format(" Return of add_auction: ~p~n", [Ret]),
  io:format(" The pid of the handler is ~p: check if it is running .... ~n",[PidHandler]),
  PidHandler ! {self(), debug},
  NewState = ServerState ++ [{ObjName, InitValue, Creator, PidHandler}],
  io:format(" New state is: ~p~n", [NewState]),
  {reply, {Ret, PidHandler}, NewState};
handle_call({get_user, Username}, _From, ServerState) ->
  Ret = mnesia_db:get_user(Username),
  {reply, Ret, ServerState};
handle_call({register, Username, Pw}, _From, ServerState) ->
  Ret = mnesia_db:add_user(Username, Pw),
  {reply, Ret, ServerState}.

handle_cast(reset, ServerState) ->
  {noreply, ServerState}.           % general format: {noreply, NewState}

