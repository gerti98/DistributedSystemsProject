package dto;

import com.ericsson.otp.erlang.*;
import java.util.ArrayList;
import java.util.List;

public class AuctionState {
    private String auctionName;
    private long remainingTime;
    private boolean winner_elected;
    private Bid winning_bid;
    private ArrayList<User> participants;
    private ArrayList<Bid> offers;


    public AuctionState(String auctionName, long remainingTime, ArrayList<User> participants, ArrayList<Bid> offers) {
        this.auctionName = auctionName;
        this.remainingTime = remainingTime;
        this.offers = offers;
        this.participants = participants;
    }

    public AuctionState(String auctionName, long remainingTime, ArrayList<User> participants, ArrayList<Bid> offers, boolean winner_elected, Bid winning_bid) {
        this(auctionName, remainingTime, participants, offers);
        this.winner_elected = winner_elected;
        this.winning_bid = winning_bid;
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

    public List<User> getParticipants() {
        return participants;
    }

    public List<Bid> getOffers() {
        return offers;
    }

    public String getAuctionName() {
        return auctionName;
    }

    public boolean isWinner_elected() {
        return winner_elected;
    }

    public Bid getWinning_bid() {
        return winning_bid;
    }


    @Override
    public String toString(){
        return "AuctionState{auction_name: " + auctionName + ", remaining_time: " + remainingTime +
                ", participants(len): " + participants.size() + ", offers(len): " + offers.size() + "}\n";
    }


    public static AuctionState decodeFromErlangTuple(OtpErlangTuple resultTuple){
        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Bid> offers = new ArrayList<>();

        String auctionname = ((OtpErlangString) (resultTuple).elementAt(1)).stringValue();
        long remainingTime = ((OtpErlangLong) (resultTuple).elementAt(2)).longValue();
        OtpErlangList userListOtp = (OtpErlangList) ((resultTuple).elementAt(3));
        for (OtpErlangObject userOtp: userListOtp){
            String username = ((OtpErlangString) userOtp).stringValue();
            System.out.println("Fetched from state user: " + username);
            userList.add(new User(username));
        }

        OtpErlangList offersListOtp = (OtpErlangList) ((resultTuple).elementAt(4));
        for(OtpErlangObject offerOtp: offersListOtp){
            OtpErlangTuple tempListOtp = (OtpErlangTuple) offerOtp;
            if(tempListOtp.elementAt(0)!=null) {
                String username = ((OtpErlangString) tempListOtp.elementAt(0)).stringValue();
                long amount = ((OtpErlangLong) tempListOtp.elementAt(1)).longValue();
                System.out.println("Fetched from offers (username: " + username + ", amount: " + amount + ")");
                offers.add(new Bid(username, amount));
            }
        }

        OtpErlangAtom winner_is_chosen = (OtpErlangAtom) ((resultTuple).elementAt(5));
        Bid winning_bid = null;
        long winning_amount;
        if(winner_is_chosen.atomValue().equals("true")){
            OtpErlangList otp_winning_bid = (OtpErlangList) ((resultTuple).elementAt(6));
            String winning_user = ((OtpErlangString) (otp_winning_bid.elementAt(0))).stringValue();
            if(winning_user.equals("NoWinner"))
                winning_amount = 0;
            else
                winning_amount = ((OtpErlangLong) (otp_winning_bid.elementAt(1))).longValue();
            System.out.println("Fetched winning bid (username: " + winning_user + ", amount: " + winning_amount + ")");
            winning_bid = new Bid(winning_user, winning_amount);
        }

        return new AuctionState(auctionname, remainingTime, userList, offers, winner_is_chosen.atomValue().equals("true"), winning_bid);
    }
}
