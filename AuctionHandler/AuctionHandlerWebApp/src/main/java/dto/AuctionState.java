package dto;

import java.util.List;

public class AuctionState {
    long remainingTime;
    List<String> participants;
    List<List<String>> offers;


    public AuctionState(long remainingTime, List<String> participants, List<List<String>> offers) {
        this.remainingTime = remainingTime;
        this.offers = offers;
        this.participants = participants;
    }

    public long getRemainingTime() {
        return remainingTime;
    }


    public List<String> getParticipants() {
        return participants;
    }

    public List<List<String>> getOffers() {
        return offers;
    }
}
