package dto;

import com.ericsson.otp.erlang.*;

import java.util.ArrayList;
import java.util.List;

public class AuctionState {
    private String auctionName;
    private long remainingTime;
    private List<String> participants;
    private List<Bid> offers;


    public AuctionState(String auctionName, long remainingTime, List<String> participants, List<Bid> offers) {
        this.auctionName = auctionName;
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

    public List<Bid> getOffers() {
        return offers;
    }

    public String getAuctionName() {
        return auctionName;
    }

    @Override
    public String toString(){
        return "AuctionState{auction_name: " + auctionName + ", remaining_time: " + remainingTime +
                ", participants(len): " + participants.size() + ", offers(len): " + offers.size() + "}\n";
    }

    public static AuctionState decodeFromErlangTuple(OtpErlangTuple resultTuple){
        List<String> userList = new ArrayList<>();
        List<Bid> offers = new ArrayList<>();

        String auctionname = ((OtpErlangString) (resultTuple).elementAt(1)).stringValue();
        long remainingTime = ((OtpErlangLong) (resultTuple).elementAt(2)).longValue();
        OtpErlangList userListOtp = (OtpErlangList) ((resultTuple).elementAt(3));
        for (OtpErlangObject userOtp: userListOtp){
            String username = ((OtpErlangString) userOtp).stringValue();
            System.out.println("Fetched from state user: " + username);
            userList.add(username);
        }

        OtpErlangList offersListOtp = (OtpErlangList) ((resultTuple).elementAt(4));
        for(OtpErlangObject offerOtp: offersListOtp){
            OtpErlangList tempListOtp = (OtpErlangList) offerOtp;
            String username = ((OtpErlangString) tempListOtp.elementAt(0)).stringValue();
            long amount = ((OtpErlangLong) tempListOtp.elementAt(1)).longValue();
            System.out.println("Fetched from offers (username: " + username + ", amount: " + amount + ")");
            offers.add(new Bid(username, amount));
        }

        return new AuctionState(auctionname, remainingTime, userList, offers);
    }
}
