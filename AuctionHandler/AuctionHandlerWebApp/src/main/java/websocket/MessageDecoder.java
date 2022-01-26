package websocket;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dto.Auction;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MessageDecoder implements Decoder.Text<List<Auction>> {
    Type listType = new TypeToken<ArrayList<Auction>>(){}.getType();

    private static Gson gson = new Gson();

    @Override
    public List<Auction> decode(String s) throws DecodeException {
        System.out.println("Received: " + s);
        List<Auction> auctionList = gson.fromJson(s, listType);
        for(Auction auction: auctionList)
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
