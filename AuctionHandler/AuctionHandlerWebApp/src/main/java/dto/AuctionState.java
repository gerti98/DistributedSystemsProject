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

    public String getFormattedTime() {
        long hours = remainingTime / 3600;
        long minutes = (remainingTime / 60) % 60;
        long seconds = remainingTime % 60;

        String hours_string = hours < 10 ? "0" + Long.toString(hours) : Long.toString(hours);
        String minutes_string = minutes < 10 ? "0" + Long.toString(minutes) : Long.toString(minutes);
        String seconds_string = seconds < 10 ? "0" + Long.toString(seconds) : Long.toString(seconds);

        return hours_string + ":" + minutes_string + ":" + seconds_string;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public List<List<String>> getOffers() {
        return offers;
    }
}
