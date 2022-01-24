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

init_auction_handler(AuctionDuration) ->
  erlang:send_after(1000, self(), {clock}),
  auction_loop({[],AuctionDuration}).


auction_loop({AuctionUsers, RemainingTime}) ->
  receive
    {_, new_user, NewUsername} ->
      NewAuctionListUsers = AuctionUsers ++ [NewUsername],
      io:format(" User added to auction user list - New list: ~p~n",[NewAuctionListUsers]),
      auction_loop({NewAuctionListUsers, RemainingTime});
    {_, del_user, DisconnectedUser} ->
      NewList = lists:delete(DisconnectedUser, AuctionUsers),
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