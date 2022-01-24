%%%-------------------------------------------------------------------
%%% @author fraie
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 22. gen 2022 13:04
%%%-------------------------------------------------------------------
-module(auction_handler).
-author("fraie").

%% API
-export([init_auction_handler/1]).
-record(offers, {user, offer_amount}).

init_auction_handler(AuctionDuration) ->
  create_offers_db(),
  start_mnesia_offer_db(),
  erlang:send_after(1000, self(), {clock}),
  auction_loop({[],AuctionDuration}).


auction_loop({AuctionUsers, RemainingTime}) ->
  receive
    {Client, new_offer, {User, Amount}} ->
      Res = add_offer(User,Amount),
      io:format("Offer Added - result: ~p~n", [Res]),
      Client ! {self(), Res},
      auction_loop({AuctionUsers, RemainingTime});
    {Client, get_offers} ->
      Result = get_offers(),
      io:format("DEBUG: Get offers return: ~p~n",[Result]),
      Client ! {self(), Result},
      auction_loop({AuctionUsers, RemainingTime});
    {Client, new_user, NewUsername} ->
      NewAuctionListUsers = AuctionUsers ++ [NewUsername],
      Client ! {self(), NewAuctionListUsers},
      io:format(" User added to auction user list - New list: ~p~n",[NewAuctionListUsers]),
      auction_loop({NewAuctionListUsers, RemainingTime});
    {Client, del_user, DisconnectedUser} ->
      NewList = lists:delete(DisconnectedUser, AuctionUsers),
      Client ! {self(), NewList},
      io:format(" User deleted from auction user list - New list: ~p~n",[NewList]),
      auction_loop({NewList, RemainingTime});
    {Client, time_req} ->
      Client ! {self(), RemainingTime},
      auction_loop({AuctionUsers, RemainingTime});
    {Client, users_online_req} ->
      Client ! {self(), AuctionUsers},
      auction_loop({AuctionUsers, RemainingTime});
    {_, debug} ->
      io:format(" dummy auction handler is running ~n"),
      auction_loop({AuctionUsers, RemainingTime});
    {clock} ->
      io:format(" Timer Updated: 1 second passed ~n"),
      NewTime = RemainingTime - 1,
      io:format(" Remaining ~p ~n",[NewTime]),
      if
        NewTime == 0 -> winner();
        NewTime > 0 -> erlang:send_after(1000, self(), {clock});
        true -> io:format("CRITICAL ERROR")
      end,
      auction_loop({AuctionUsers, NewTime});
    _ ->
      auction_loop({AuctionUsers, RemainingTime})
  end.


winner() ->
  io:format(" DUMMY: The winner is ?? ~n").


%% SUPPORT DB
create_offers_db() ->
  mnesia:create_schema([node()]),
  application:start(mnesia),
  io:format("Test debug: Offer DB started ~n"),
  mnesia:create_table(offers, [
    {attributes, record_info(fields, offers)}, {disc_copies, [node()]}]).

%% @doc This function start an already existing mnesia server
start_mnesia_offer_db() ->
  application:start(mnesia).

%% @doc This function a running instance of the mnesia server (the
%% information are maintained on the disk).
stop_mnesia_offer_db() ->
  application:stop(mnesia).

add_offer(Username, Amount) ->
  F = fun() -> mnesia:write(#offers{user = Username, offer_amount = Amount}) end,
  mnesia:transaction(F).

get_offers() ->
  R = fun() ->
    io:format("Retrieving offers ~n"),
    User = #offers{user = '$1', offer_amount = '$2', _ = '_'},
    mnesia:select(offers, [{User, [], [['$1', '$2']]}])
      end,
  mnesia:transaction(R).
