package dto;

import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class Auction extends OtpErlangMap{
    String goodName;
    int startingValue;
    String username;

    public Auction(String goodName, int startingValue, String username) {
        super(
                new OtpErlangObject[]{new OtpErlangString("goodName"), new OtpErlangString("startingValue"), new OtpErlangString("username")},
                new OtpErlangObject[]{new OtpErlangString(goodName), new OtpErlangInt(startingValue), new OtpErlangString(username)}
        );
        this.goodName = goodName;
        this.startingValue = startingValue;
        this.username = username;
    }

    public String getGoodName() {
        return goodName;
    }

    public int getStartingValue() {
        return startingValue;
    }

    public String getUsername() {
        return username;
    }
}
