package websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.Auction;
import dto.AuctionState;

import javax.websocket.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AuctionStateDecoder implements Decoder.Text<AuctionState> {
    private static Gson gson = new Gson();

    @Override
    public AuctionState decode(String s) throws DecodeException {
        System.out.println("(AuctionStateDecoder) Received: " + s);
        System.out.println("Debug1");
        AuctionState auctionState = gson.fromJson(s, AuctionState.class);
        System.out.println("Debug2");
        System.out.println("AuctionState object: " + auctionState);
        return auctionState;
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

