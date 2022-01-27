package websocket;

import dto.Auction;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/main_menu_endpoint/{username}", decoders = AuctionListDecoder.class, encoders = AuctionListEncoder.class)
public class MainMenuEndpoint {
    private Session session;
    private static final Set<MainMenuEndpoint> auctionEndpoints = new CopyOnWriteArraySet<MainMenuEndpoint>();
    private static HashMap<String, String> users = new HashMap<String, String>();

    @OnOpen
    public void onOpen(Session session, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        auctionEndpoints.add(this);
        users.put(session.getId(), username);

        System.out.println("[MAIN MENU ENDPOINT] Current users joined");
        for(String user: users.values())
            System.out.println(user);
    }

    @OnMessage
    public void onMessage(Session session, List<Auction> auctionList) throws IOException, EncodeException {
        System.out.println("OnMessage");
        System.out.println("Auction list is going to be broadcast");
        broadcast(auctionList);
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        auctionEndpoints.remove(this);
        users.remove(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void broadcast(List<Auction> auctionList) throws IOException, EncodeException {
        auctionEndpoints.forEach(endpoint -> {
            synchronized (endpoint) {
                try {
                    endpoint.session.getBasicRemote()
                        .sendObject(auctionList);
                } catch (IOException | EncodeException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
