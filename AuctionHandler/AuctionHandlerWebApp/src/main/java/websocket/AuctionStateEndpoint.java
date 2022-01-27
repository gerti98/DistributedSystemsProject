package websocket;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import dto.Auction;
import dto.AuctionState;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ServerEndpoint(value = "/auction_state/{auction}/{username}", decoders = AuctionStateDecoder.class, encoders = AuctionStateEncoder.class)
public class AuctionStateEndpoint{
    private Session session;

    private static final Multimap<String, UserEndpointPair> clientEndpointByAuction = Multimaps.synchronizedMultimap(ArrayListMultimap.create());

    @OnOpen
    public void onOpen(Session session, @PathParam("auction") String auction, @PathParam("username") String username) throws IOException, EncodeException {
        this.session = session;
        clientEndpointByAuction.put(auction, new UserEndpointPair(username, this));

        System.out.println("Auction endpoint status");
        Set<String> keys = clientEndpointByAuction.keySet();
        for(String key: keys){
            System.out.println("Auction: " + key);
            Collection<UserEndpointPair> userEndpointPairList = clientEndpointByAuction.get(key);

            for(UserEndpointPair userEndpointPair: userEndpointPairList){
                System.out.println("User: " + userEndpointPair.getUsername());
            }
        }
    }

    @OnMessage
    public void onMessage(Session session, AuctionState auctionState) throws IOException, EncodeException {
        System.out.println("OnMessage auction state endpoint");
        auctionBroadcast(auctionState);
    }

    @OnClose
    public void onClose(Session session, @PathParam("auction") String auction, @PathParam("username") String username) throws IOException, EncodeException {
        System.out.println("User: " + username + " is exiting the auction: " + auction);
        clientEndpointByAuction.remove(auction, new UserEndpointPair(username, this));
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

    private static void auctionBroadcast(AuctionState auctionState) throws IOException, EncodeException {
        System.out.println("auction broadcast for " + auctionState);
        Collection<UserEndpointPair> userEndpointPairList = clientEndpointByAuction.get(auctionState.getAuctionName());
        userEndpointPairList.forEach(userEndpointPair -> {
            synchronized (userEndpointPair) {
                try{
                    System.out.println("User " + userEndpointPair.getUsername() + " will soon receive a message");
                    userEndpointPair.getEndpoint().session.getBasicRemote().sendObject(auctionState);
                } catch (EncodeException | IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}

