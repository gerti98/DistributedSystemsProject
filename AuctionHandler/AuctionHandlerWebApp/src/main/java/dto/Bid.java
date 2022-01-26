package dto;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class Bid {
    String username;
    long bid;

    public Bid(String username, Long bid) {
        this.username = username;
        this.bid = bid;
    }

    public String getUsername() {
        return username;
    }

    public long getBid() {
        return bid;
    }

    @Override
    public String toString(){
        return "BidObject{username: " + username + ", bid: " + bid + "}\n";
    }

    public OtpErlangMap encodeInErlangMap(){
        return new OtpErlangMap(
                new OtpErlangObject[]{new OtpErlangString("username"), new OtpErlangString("bid")},
                new OtpErlangObject[]{new OtpErlangString(username), new OtpErlangLong(bid)}
        );
    }
}
