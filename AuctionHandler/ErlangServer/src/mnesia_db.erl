%%%-------------------------------------------------------------------
%%% @author fraie
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 20. gen 2022 16:17
%%%-------------------------------------------------------------------
-module(mnesia_db).
-author("fraie").

%% API
-export([create_mnesia_db/0, start_mnesia/0, stop_mnesia_db/0, add_user/2, get_user/1]).
-record(user, {name, password}).

%% @doc This function creates a mnesia server. It must be called once at
%% the beginning of the application life cycle.
create_mnesia_db() ->
  mnesia:create_schema([node()]),
  application:start(mnesia),
   io:format("Test debug ~n"),
  mnesia:create_table(user, [
    {attributes, record_info(fields, user)}, {disc_copies, [node()]}]).

%% disc copies means that the db is in RAM memory and also on the disk

%% mnesia:create_table(user, [
%%    {attributes, record_info(fields, user)},
%%    {index, [#user.name]},
%%    {disc_copies, self()}]).


%% @doc This function start an already existing mnesia server
start_mnesia() ->
  application:start(mnesia).

%% @doc This function a running instance of the mnesia server (the
%% information are mainteined on the disk).
stop_mnesia_db() ->
  application:stop(mnesia).

add_user(Username, Password) ->
  F = fun() -> mnesia:write(#user{name=Username, password = Password}) end,
  mnesia:transaction(F).

get_user(Username_to_find) ->
  R = fun() ->
    io:format("Searching for ~s~n", [Username_to_find]),
    User = #user{name='$1', password = '$2', _ = '_'},
    Guard = {'==', '$1', Username_to_find},
    mnesia:select(user, [{User, [Guard], [['$1', '$2']]}])
      end,
  mnesia:transaction(R).