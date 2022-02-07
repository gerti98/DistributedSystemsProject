package websocket;
import dto.AuctionState;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint(value = "/auction_state/{auction}/{username}", decoders = AuctionStateDecoder.class, encoders = AuctionStateEncoder.class)
public class AuctionStateEndpoint{
    private Session session;

    private static final ConcurrentMap<String, Set<UserEndpointPair>> clientEndpointByAuction = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("auction") String auction, @PathParam("username") String username) throws IOException, EncodeException {
        System.out.println("[AUCTION STATE ENDPOINT] OnOpen of auction: " + auction + ", and user: " + username);
        this.session = session;

        if(clientEndpointByAuction.get(auction) == null){
            Set<UserEndpointPair> userEndpointPairs = new CopyOnWriteArraySet<>();
            userEndpointPairs.add(new UserEndpointPair(username, this));
            clientEndpointByAuction.put(auction, userEndpointPairs);
        } else {
            clientEndpointByAuction.get(auction).add(new UserEndpointPair(username, this));
        }
        printEndpointStatus();
    }

    @OnMessage
    public void onMessage(Session session, AuctionState auctionState) throws IOException, EncodeException {
        System.out.println("[AUCTION STATE ENDPOINT] OnMessage");
        auctionBroadcast(auctionState);
    }

    @OnClose
    public void onClose(Session session, @PathParam("auction") String auction, @PathParam("username") String username) throws IOException, EncodeException {
        System.out.println("[AUCTION STATE ENDPOINT] OnClose, User: " + username + " is exiting the auction: " + auction);
        Set<UserEndpointPair> userEndpointPairSet = clientEndpointByAuction.get(auction);
        for(Iterator<UserEndpointPair> iterator = userEndpointPairSet.iterator(); iterator.hasNext();){
            UserEndpointPair current = iterator.next();
            if(username.equals(current.getUsername())){
                boolean result2 = userEndpointPairSet.remove(current);
                System.out.println("Delete result in cycle: " + result2);
            }
        }

        printEndpointStatus();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void auctionBroadcast(AuctionState auctionState) throws IOException, EncodeException {
        System.out.println("[AUCTION STATE ENDPOINT] auction state broadcast for " + auctionState);
        Collection<UserEndpointPair> userEndpointPairList = clientEndpointByAuction.get(auctionState.getAuctionName());
        userEndpointPairList.forEach(userEndpointPair -> {
            try{
                System.out.println("User " + userEndpointPair.getUsername() + " will soon receive a message");
                userEndpointPair.getEndpoint().session.getBasicRemote().sendObject(auctionState);
            } catch (EncodeException | IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void printEndpointStatus(){
        System.out.println("[AUCTION STATE ENDPOINT] Endpoint Status");
        Set<String> keys = clientEndpointByAuction.keySet();
        for(String key: keys){
            Collection<UserEndpointPair> userEndpointPairList = clientEndpointByAuction.get(key);

            for(UserEndpointPair userEndpointPair: userEndpointPairList){
                System.out.println("[Auction: " + key + "] User: " + userEndpointPair.getUsername());
            }
        }
    }
}

