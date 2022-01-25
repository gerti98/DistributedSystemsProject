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
-export([create_mnesia_db/0, start_mnesia/0, stop_mnesia_db/0, add_user/2, get_user/1, add_auction/6, get_active_auctions/0, get_auction/1]).

-record(user, {name, password}).
-record(auction, {name, duration, startingValue, imageURL, creator, pid}).

%% @doc This function creates a mnesia server. It must be called once at
%% the beginning of the application life cycle.
create_mnesia_db() ->
  mnesia:create_schema([node()]),
  application:start(mnesia),
   io:format("Test debug: Main server started ~n"),
  mnesia:create_table(user, [
    {attributes, record_info(fields, user)}, {disc_copies, [node()]}]),
  mnesia:create_table(auction, [
    {attributes, record_info(fields, auction)}, {disc_copies, [node()]}]).
%% disc copies means that the db is in RAM memory and also on the disk

%% mnesia:create_table(user, [
%%    {attributes, record_info(fields, user)},
%%    {index, [#user.name]},
%%    {disc_copies, self()}]).


%% @doc This function start an already existing mnesia server
start_mnesia() ->
  application:start(mnesia).

%% @doc This function a running instance of the mnesia server (the
%% information are maintained on the disk).
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

add_auction(ObjectName, Duration, InitialValue, ImageURL, Creator, Pid) ->
  io:format(" Adding Auction ~p ~p ~p ~p ~p ~p ~n", [ObjectName, Duration, InitialValue, ImageURL, Creator, Pid]),
  F = fun() -> mnesia:write(#auction{name=ObjectName, duration=Duration, startingValue = InitialValue, imageURL = ImageURL, creator = Creator, pid = Pid}) end,
  mnesia:transaction(F).


get_active_auctions() ->
  io:format("Getting Active Auctions List~n"),
  F = fun() ->
    Auction = #auction{name='$1', duration='$2', startingValue='$3', imageURL = '$4', creator='$5', pid='$6', _ = '_'},
    mnesia:select(auction, [{Auction, [], [['$1', '$2', '$3', '$4', '$5', '$6']]}])
      end,
  mnesia:transaction(F).

get_auction(Object_name_to_find) ->
  R = fun() ->
    io:format("Searching for ~s~n", [Object_name_to_find]),
    Auction = #auction{name='$1', duration='$2', startingValue='$3', imageURL = '$4', creator='$5', pid='$6', _ = '_'},
    Guard = {'==', '$1', Object_name_to_find},
    mnesia:select(auction, [{Auction, [Guard], [['$1', '$2', '$3', '$4', '$5', '$6']]}])
      end,
  mnesia:transaction(R).