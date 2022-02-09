package websocket;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dto.Auction;
import dto.AuctionList;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class AuctionListEncoder implements Encoder.Text<AuctionList> {

    private static Gson gson = new Gson();

    @Override
    public String encode(AuctionList message) throws EncodeException {
        String json = gson.toJson(message);
        System.out.println("Message encoded in JSON: " + message);
        return json;
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
