package websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.Auction;
import dto.AuctionList;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AuctionListDecoder implements Decoder.Text<AuctionList> {
    private static Gson gson = new Gson();

    @Override
    public AuctionList decode(String s) throws DecodeException {
        System.out.println("[AuctionListDecoder] Received: " + s);
        AuctionList auctionList = gson.fromJson(s, AuctionList.class);
        for(Auction auction: auctionList.getAuctionList())
            System.out.println(auction.toString());
        return auctionList;
    }

    @Override
    public boolean willDecode(String s) {
        return (s != null);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
