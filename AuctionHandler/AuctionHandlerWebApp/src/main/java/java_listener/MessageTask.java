package java_listener;

import com.ericsson.otp.erlang.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import dto.Auction;
import dto.AuctionState;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MessageTask implements Runnable{
    private OtpErlangObject message;
    private static final String base_uri = "localhost:8080/AuctionHandlerWebApp_war_exploded";

    public MessageTask(OtpErlangObject message)
    {
        this.message = message;
    }

    public void run()
    {

        if(message instanceof OtpErlangTuple){
            //{self(), destinationAtom, {ResultTuple}}
            OtpErlangTuple resultTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(2);
            OtpErlangAtom destination_atom = (OtpErlangAtom) ((OtpErlangTuple) message).elementAt(1);

            //TODO: add branching for auction state
            if(destination_atom.atomValue().equals("auction_list")){
                System.out.println("Refresh of list");
                OtpErlangList resultList = (OtpErlangList) (resultTuple).elementAt(1);
                try {
                    refreshMainMenu(resultList);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if(destination_atom.atomValue().equals("update_auction_state")){
                System.out.println("Refresh Auction");
                try {
                    refreshAuctionPage(resultTuple);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void refreshMainMenu(OtpErlangList resultList) throws IOException {
        //TODO: maybe we can cache websocket connections
        WebsocketClientEndpoint websocketClientEndpoint = null;
        List<Auction> auctionList = new ArrayList<>();
        try {
            websocketClientEndpoint = new WebsocketClientEndpoint(new URI("ws://" + base_uri + "/main_menu_endpoint/listener"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        for(OtpErlangObject result : resultList){
            Auction auction = Auction.decodeFromErlangList((OtpErlangList) result);
            System.out.println("Fetched: " + auction);
            auctionList.add(auction);
        }
        websocketClientEndpoint.sendMessage(new Gson().toJson(auctionList));
        websocketClientEndpoint.userSession.close();
    }

    private void refreshAuctionPage(OtpErlangTuple resultTuple) throws IOException {
        WebsocketClientEndpoint websocketClientEndpoint = null;
        AuctionState auctionState = AuctionState.decodeFromErlangTuple(resultTuple);
        System.out.println("AuctionState to be sent: " + auctionState);
        try {
            websocketClientEndpoint = new WebsocketClientEndpoint(new URI("ws://" + base_uri + "/auction_state/"+auctionState.getAuctionName()+"/listener"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String jsonString = new ObjectMapper().writeValueAsString(auctionState);
        System.out.println("Auction state in json : " + jsonString);
        websocketClientEndpoint.sendMessage(jsonString);
        websocketClientEndpoint.userSession.close();
    }
}
