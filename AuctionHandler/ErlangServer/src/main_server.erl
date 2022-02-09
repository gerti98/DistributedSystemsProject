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
-export([start_main_server/0, get_user_list/0, get_active_auction_list/0, create_new_auction/5, reset/0, get_user_by_username/1, register_user/2, endpoint_loop/0, login_user/2, update_auction/2, get_passed_auction_list/0, update_auction_pid/2]).
-export([init/1, handle_call/3, handle_cast/2]). %% Necessary otherwise nothing work

% API functions

start_main_server() ->
  mnesia_db:create_mnesia_db(),
  Return = gen_server:start({local, main_server}, ?MODULE, [], []),
  io:format(" [MAIN SERVER] start link main server: ~p~n", [Return]),
  Pid = spawn(?MODULE, endpoint_loop, []),
  register(main_server_endpoint, Pid),
  Return.

%% ---- IMPORTANT ---
%% Expected reply for correct operation (login, registration) from server: {self(), {ok}}
%% Expected reply for any error on operation from server: {self(), {false}}
%% I.E.
%% The client will send a message like this: {#Pid<client@localhost.1.0>,login,#{"username" => "Pippo","password" => "Pippo"}}
%% The server will reply with {self(), {ok}} if User "Pippo" exists and the related pwd is "Pippo", false otherwise
endpoint_loop() ->
  receive
    {ClientPid, register, MessageMap} ->
      io:format(" [MAIN SERVER] Received a register message~n"),
      Result = register_user(maps:get("username", MessageMap), maps:get("password", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, login, MessageMap} ->
      io:format(" [MAIN SERVER] Received a login message~n"),
      Result = login_user(maps:get("username", MessageMap), maps:get("password", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, create_auction, MessageMap} ->
      io:format(" [MAIN SERVER] Received a create auction message~n"),
      Result = create_new_auction(maps:get("goodName", MessageMap), maps:get("duration", MessageMap),  maps:get("startingValue", MessageMap), maps:get("imageURL", MessageMap), maps:get("username", MessageMap)),
      ClientPid ! {self(), Result},
      %%Additional part
      AuctionListResult = get_active_auction_list(),
      {mbox, listener@localhost} ! {self(), active_auction_list, AuctionListResult};
    {ClientPid, get_auction_pid, MessageMap} ->
      io:format(" [MAIN SERVER] Received a get auction pid~n"),
      Result = get_auction_pid(maps:get("goodName", MessageMap)),
      ClientPid ! {self(), Result};
    {ClientPid, get_active_auctions} ->
      io:format(" [MAIN SERVER] Received a get active auctions message~n"),
      Result = get_active_auction_list(),
      ClientPid ! {self(), Result};
    {ClientPid, get_passed_auctions} ->
      io:format(" [MAIN SERVER] Received a get passed auctions message~n"),
      Result = get_passed_auction_list(),
      ClientPid ! {self(), Result};
    {update_win, NameAuction, Winner} ->
      io:format(" [MAIN SERVER] Received a request for update the winner of an auction ~n"),
      _Result = update_auction(NameAuction, Winner),
      %%AuctionPid ! {self(), Result};
      AuctionListResult = get_active_auction_list(),
      {mbox, listener@localhost} ! {self(), active_auction_list, AuctionListResult},
      PassedAuctionListResult = get_passed_auction_list(),
      {mbox, listener@localhost} ! {self(), passed_auction_list, PassedAuctionListResult};
    {update_pid, NameAuction, Pid} ->
      io:format(" [MAIN SERVER] Received a request for update the pid of an auction ~n"),
      _Result = update_auction_pid(NameAuction, Pid);
    _ -> io:format(" [MAIN SERVER] Received any message~n")
  end,
  endpoint_loop().


get_user_list() ->
  gen_server:call(main_server, user_list).

get_user_by_username(Username) ->
  gen_server:call(main_server, {get_user, Username}).

register_user(Username, Pw) ->
  case gen_server:call(main_server, {register, Username, Pw}) of
    {atomic, ok} -> {ok};
    _ -> {false}
  end.

login_user(Username, Pw) ->
  case gen_server:call(main_server, {get_user, Username}) of
    {atomic, [UserTuple | _]} ->
      %% The second field of UserTuple is the Password
      case lists:nth(2, UserTuple) == Pw of
        true -> {ok};
        false -> {false}
      end;
    _ -> {false}
  end.


get_active_auction_list() ->
  case gen_server:call(main_server, auction_list) of
    {atomic, AuctionList} ->
      {ok, AuctionList};
    _ -> {false}
  end.

get_passed_auction_list() ->
  case gen_server:call(main_server, passed_auction_list) of
    {atomic, AuctionList} ->
      {ok, AuctionList};
    _ -> {false}
  end.


create_new_auction(ObjName, Duration, InitValue, ImageURL, Creator) ->
  case gen_server:call(main_server, {new_auction, ObjName, Duration, InitValue, ImageURL, Creator}) of
    {{atomic, ok}, PidAuctionHandler} -> {ok, [PidAuctionHandler]};
    _ -> {false}
  end.

update_auction(AuctionName, Winner) ->
  gen_server:call(main_server, {update_win, AuctionName, Winner}).

update_auction_pid(AuctionName, Pid) ->
  gen_server:call(main_server, {update_pid, AuctionName, Pid}).

get_auction_pid(ObjName) ->
  case gen_server:call(main_server, {get_auction_pid, ObjName}) of
    {atomic, PidAuctionInList} -> {ok, PidAuctionInList};
    _ -> {false}
  end.

reset() ->
  gen_server:cast(main_server, reset).


% gen_server CALLBACK FUNCTIONS
%% The server state maintain the list of active auctions
init([]) ->
  mnesia_db:start_mnesia(),
  {ok, []}.   % general format: {ok, InitialState}

handle_call(user_list, _From, _ServerState) ->
  {reply, {todo, da, user_list_call}, []};
handle_call(auction_list, _From, ServerState) ->
  Ret = mnesia_db:get_active_auctions(),
  io:format(" [MAIN SERVER] Result: ~p~n", [Ret]),
  {reply, Ret, ServerState};
handle_call(passed_auction_list, _From, ServerState) ->
  Ret = mnesia_db:get_passed_auctions(),
  io:format(" [MAIN SERVER] Result: ~p~n", [Ret]),
  {reply, Ret, ServerState};
handle_call({new_auction, ObjName, Duration, InitValue, ImageURL, Creator}, _From, ServerState) ->
  PidHandler = spawn( fun() -> auction_handler:init_auction_handler(ObjName, Duration) end),
  Ret = mnesia_db:add_auction(ObjName, Duration, InitValue, ImageURL, Creator, PidHandler),
  io:format(" [MAIN SERVER] Return of add_auction: ~p~n", [Ret]),
  case Ret == {atomic, false} of
    true ->
      io:format(" [MAIN SERVER] The process ~p must be killed~n",[PidHandler]),
      exit(PidHandler, kill),
      {reply, {Ret, 0}, ServerState};
    false -> io:format(" [MAIN SERVER] The pid of the handler is ~p~n",[PidHandler]),
      NewState = ServerState ++ [{ObjName, Duration, InitValue, Creator, PidHandler}],
      io:format(" [MAIN SERVER] New state is: ~p~n", [NewState]),
      io:format(" [MAIN SERVER] Adding the process ~p to the auctions monitor ~n", [NewState]),
      AuctionsMonitorPid = whereis(auctions_monitor),
      AuctionsMonitorPid ! {add_auction_to_monitor, PidHandler, ObjName, Duration},
      {reply, {Ret, PidHandler}, NewState}
  end;
handle_call({get_auction_pid, ObjName}, _From, _ServerState) ->
  Ret = mnesia_db:get_auction_pid(ObjName),
  io:format(" [MAIN SERVER] Return of get_auction_pid: ~p~n", [Ret]),
  {reply, Ret, []};
handle_call({get_user, Username}, _From, _ServerState) ->
  Ret = mnesia_db:get_user(Username),
  {reply, Ret, []};
handle_call({register, Username, Pw}, _From, _ServerState) ->
  Ret = mnesia_db:add_user(Username, Pw),
  {reply, Ret, []};
handle_call({update_win, AuctionName, Winner}, _From, _ServerState) ->
  Ret = mnesia_db:update_auction_winner(AuctionName, Winner),
  io:format(" [MAIN SERVER] Result from auction update ~p~n",[Ret]),
  {reply, Ret, []};
handle_call({update_pid, AuctionName, Pid}, _From, _ServerState) ->
  Ret = mnesia_db:update_auction_pid(AuctionName, Pid),
  io:format(" [MAIN SERVER] Result from auction update pid ~p~n",[Ret]),
  {reply, Ret, []}.

handle_cast(reset, ServerState) ->
  {noreply, ServerState}.           % general format: {noreply, NewState}
