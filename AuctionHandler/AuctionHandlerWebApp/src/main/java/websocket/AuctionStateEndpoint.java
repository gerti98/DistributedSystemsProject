package websocket;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dto.AuctionState;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.text.CollationElementIterator;
import java.util.*;

@ServerEndpoint(value = "/auction_state/{auction}/{username}", decoders = AuctionStateDecoder.class, encoders = AuctionStateEncoder.class)
public class AuctionStateEndpoint{
    private Session session;

    private static final Multimap<String, UserEndpointPair> clientEndpointByAuction = Multimaps.synchronizedMultimap(ArrayListMultimap.create());

    @OnOpen
    public void onOpen(Session session, @PathParam("auction") String auction, @PathParam("username") String username) throws IOException, EncodeException {
        System.out.println("[AUCTION STATE ENDPOINT] OnOpen of auction: " + auction + ", and user: " + username);
        this.session = session;
        clientEndpointByAuction.put(auction, new UserEndpointPair(username, this));
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
        Collection<UserEndpointPair> userEndpointPairList = clientEndpointByAuction.get(auction);
        for(Iterator<UserEndpointPair> iterator = userEndpointPairList.iterator(); iterator.hasNext();){
            UserEndpointPair current = iterator.next();
            if(username.equals(current.getUsername())){
                boolean result2 = clientEndpointByAuction.remove(auction, current);
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

