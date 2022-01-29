package dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ProvaSerialize {
    public static void main(String[] args) throws JsonProcessingException {
        ArrayList<User> participants = new ArrayList<>();
        participants.add(new User("Pippo"));
        participants.add(new User("Pluto"));

        ArrayList<Bid> bids = new ArrayList<>();
        bids.add(new Bid("Pippo", 100L));
        bids.add(new Bid("Pippo", 200L));

        AuctionState auctionState = new AuctionState("auction", 1000, participants, bids);

        String jsonString = new Gson().toJson(auctionState);
        System.out.println("Auction state in json : " + jsonString);

        AuctionState auctionState1 = new Gson().fromJson(jsonString, AuctionState.class);
        System.out.println("Auction state reborn : " + auctionState1);

    }
}
