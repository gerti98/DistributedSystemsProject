package websocket;

public class UserEndpointPair {
    String username;
    AuctionStateEndpoint endpoint;
    public UserEndpointPair(String username, AuctionStateEndpoint endpoint){
        this.username = username;
        this.endpoint = endpoint;
    }

    public String getUsername() {
        return username;
    }

    public AuctionStateEndpoint getEndpoint() {
        return endpoint;
    }
}
