package websocket;

import com.google.gson.Gson;
import dto.Auction;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.List;

public class MessageEncoder implements Encoder.Text<List<Auction>> {

    private static Gson gson = new Gson();

    @Override
    public String encode(List<Auction> message) throws EncodeException {
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
