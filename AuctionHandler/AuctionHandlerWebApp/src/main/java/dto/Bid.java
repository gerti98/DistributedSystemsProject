package dto;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class Bid extends OtpErlangMap {
    String username;
    long bid;

    public Bid(String username, Long bid) {
        super(
                new OtpErlangObject[]{new OtpErlangString("username"), new OtpErlangString("bid")},
                new OtpErlangObject[]{new OtpErlangString(username), new OtpErlangLong(bid)}
        );
        this.username = username;
        this.bid = bid;
    }

    public String getUsername() {
        return username;
    }

    public long getBid() {
        return bid;
    }
}
