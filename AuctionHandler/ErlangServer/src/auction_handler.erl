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
-export([init_auction_handler/3]).

init_auction_handler(AuctionName, AuctionDuration, MinimumOffer) ->
  erlang:send_after(1000, self(), {clock}),
  auction_loop({AuctionName, [], AuctionDuration, [], MinimumOffer}).


auction_loop({AuctionName, AuctionUsers, RemainingTime, OfferList, MinimumOffer}) ->
  receive
    {Client, new_offer, MessageMap} ->
      io:format(" [AUCTION HANDLER] New Bid: ~p~n", [MessageMap]),
      CurrentBid = maps:get("bid", MessageMap),

      case OfferList of
        [First | _] -> BestOffer = First;
        [] -> BestOffer = {"", MinimumOffer}
      end,
      BestBid = element(2, BestOffer),

      if
        BestBid < CurrentBid -> NewOfferList = [{maps:get("username", MessageMap), maps:get("bid", MessageMap)}] ++ OfferList;
        true ->
          NewOfferList = OfferList,
          io:format(" [AUCTION HANDLER] this offer is too low ~n")
      end,

      ToSend = {ok, AuctionName, RemainingTime, AuctionUsers, NewOfferList, false},
      io:format(" [AUCTION HANDLER] Sending state: ~p~n", [ToSend]),
      Client ! {self(), ToSend},
      {mbox, listener@localhost} ! {self(), update_auction_state, ToSend},
      auction_loop({AuctionName, AuctionUsers, RemainingTime, NewOfferList, MinimumOffer});
    {Client, new_user, MessageMap} ->
      io:format(" [AUCTION HANDLER] Received new_user message~n"),
      NewAuctionListUsers = AuctionUsers ++ [maps:get("username", MessageMap)],
      ToSend = {ok, AuctionName, RemainingTime, NewAuctionListUsers, OfferList, false},
      Client ! {self(), ToSend},
      {mbox, listener@localhost} ! {self(), update_auction_state, ToSend},
      io:format(" [AUCTION HANDLER] User added to auction user list - New list: ~p~n",[NewAuctionListUsers]),
      auction_loop({AuctionName, NewAuctionListUsers, RemainingTime, OfferList, MinimumOffer});
    {Client, del_user, MessageMap} ->
      DisconnectedUser = maps:get("username", MessageMap),
      NewList = lists:delete(DisconnectedUser, AuctionUsers),
      ToSend = {ok, AuctionName, RemainingTime, NewList, OfferList, false},
      {mbox, listener@localhost} ! {self(), update_auction_state, ToSend},
      io:format(" [AUCTION HANDLER] User deleted from auction user list - New list: ~p~n",[NewList]),
      auction_loop({AuctionName, NewList, RemainingTime, OfferList, MinimumOffer});
    {Client, get_auction_state} ->
      io:format(" [AUCTION HANDLER] Requested auction state ~n"),
      ToSend = {ok, AuctionName, RemainingTime, AuctionUsers, OfferList, false},
      io:format(" [AUCTION HANDLER] Sending state: ~p~n", [ToSend]),
      Client ! {self(), ToSend},
      auction_loop({AuctionName, AuctionUsers, RemainingTime, OfferList, MinimumOffer});
    {Sender, kill_auction_suicide} ->
      case Sender==self() of
        true -> io:format(" [AUCTION HANDLER] Suicide: ~p killed...~n", [self()]);
        false ->  io:format(" [AUCTION HANDLER] Kill request ignored ~n"),
                  auction_loop({AuctionName, AuctionUsers, RemainingTime, OfferList,  MinimumOffer})
      end;
    {clock} ->
      io:format(" [AUCTION HANDLER] Timer Updated: 1 second passed ~n"),
      NewTime = RemainingTime - 1,
      io:format(" [AUCTION HANDLER] Remaining ~p ~n",[NewTime]),
      if
        NewTime == 0 ->   winner(AuctionName, RemainingTime, AuctionUsers, OfferList),
                          erlang:send_after(5000, self(), {self(), kill_auction_suicide});
        NewTime > 0 -> erlang:send_after(1000, self(), {clock});
        true -> io:format("CRITICAL ERROR")
      end,
      auction_loop({AuctionName, AuctionUsers, NewTime, OfferList, MinimumOffer});
    _ ->
      io:format(" [AUCTION HANDLER] Unrecognized message arrived, skipping... ~n"),
      auction_loop({AuctionName, AuctionUsers, RemainingTime, OfferList, MinimumOffer})
  end.

winner(AuctionName, RemainingTime, AuctionUsers, []) ->
  io:format(" [AUCTION HANDLER] No offers and no winner for this auction ~n"),
  ToSend = {ok, AuctionName, RemainingTime, AuctionUsers, [], true, ["NoWinner", 0]},
  {mbox, listener@localhost} ! {self(), update_auction_state, ToSend},
  MainServerPid = whereis(main_server_endpoint),
  io:format(" [AUCTION HANDLER] Sending update request to ~p ~n", [MainServerPid]),
  MainServerPid ! {update_win, AuctionName, "NoWinner"};
winner(AuctionName, RemainingTime, AuctionUsers, OfferList) ->
  io:format(" [AUCTION HANDLER] Deciding the winner ~n"),
  FinalOffersAmount = get_offers_amount(OfferList),
  FinalOffersUsers = get_offers_users(OfferList),
  io:format(" [AUCTION HANDLER] Final Offers Amount ~p~n", [FinalOffersAmount]),
  io:format(" [AUCTION HANDLER] Final Offers Users ~p~n", [FinalOffersUsers]),
  MaxIndex = 0,
  io:format(" [AUCTION HANDLER] The Winner is in position ~p~n",[MaxIndex]),
  [Winner | _] = FinalOffersUsers,
  [WinningBid | _] = FinalOffersAmount,
  io:format(" [AUCTION HANDLER] Among the users ~p the winner is ~p~n", [FinalOffersUsers, Winner]),
  io:format(" [AUCTION HANDLER] Among the offers ~p the winning bid is ~p~n", [FinalOffersAmount, WinningBid]),
  ToSend = {ok, AuctionName, RemainingTime, AuctionUsers, OfferList, true, [Winner, WinningBid]},
  {mbox, listener@localhost} ! {self(), update_auction_state, ToSend},
  MainServerPid = whereis(main_server_endpoint),
  io:format(" [AUCTION HANDLER] Sending update request to ~p ~n", [MainServerPid]),
  MainServerPid ! {update_win, AuctionName, Winner}.

get_offers_amount(OfferList) ->
  OffersAmountList = [Amount || {_User, Amount} <- OfferList],
  io:format(" OffersAmountList: ~p",[OffersAmountList]),
  OffersAmountList.

get_offers_users(OfferList) ->
  OffersUsersList = [User || {User, _Amount} <- OfferList],
  io:format(" OffersUserList: ~p",[OffersUsersList]),
  OffersUsersList.
