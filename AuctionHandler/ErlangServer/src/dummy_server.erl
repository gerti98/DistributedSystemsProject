%%%-------------------------------------------------------------------
%%% @author gxhan
%%% @copyright (C) 2022, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 19. Jan 2022 20:00
%%%-------------------------------------------------------------------
-module(dummy_server).
-author("gxhan").

%% API
-export([listen_and_reply/0]).


listen_and_reply() ->
  receive
    {PeerAddress, Content} ->
      PeerAddress ! Content,
      Content
  end.