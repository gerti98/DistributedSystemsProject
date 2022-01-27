package websocket;

import com.google.gson.Gson;
import dto.Auction;
import dto.AuctionState;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class AuctionStateEncoder implements Encoder.Text<AuctionState> {

    private static Gson gson = new Gson();

    @Override
    public String encode(AuctionState message) throws EncodeException {
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

