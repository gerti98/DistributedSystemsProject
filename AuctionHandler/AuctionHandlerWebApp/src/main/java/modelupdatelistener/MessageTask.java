package modelupdatelistener;

import com.ericsson.otp.erlang.*;
import com.google.gson.Gson;
import dto.Auction;

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
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(2);
            OtpErlangAtom destination_atom = (OtpErlangAtom) (resulTuple).elementAt(0);

            //TODO: add branching for auction state
            if(destination_atom.toString().equals("auction_list")){
                OtpErlangList resultList = (OtpErlangList) (resulTuple).elementAt(1);
                refreshMainMenu(resultList);
            } else {

            }

        }
    }


    private void refreshMainMenu(OtpErlangList resultList){
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
    }
}
