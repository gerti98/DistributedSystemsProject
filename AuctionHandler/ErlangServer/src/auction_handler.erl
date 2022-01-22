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
-export([auction_loop/0]).

auction_loop() ->
  receive
    {_, debug} ->
      io:format(" dummy auction handler is running ~n"),
      auction_loop();
    _ ->
      auction_loop()
  end.