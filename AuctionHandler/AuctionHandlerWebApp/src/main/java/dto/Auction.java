package dto;

import com.ericsson.otp.erlang.OtpErlangInt;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class Auction extends OtpErlangMap{
    String goodName;
    int startingValue;

    public Auction(String goodName, int startingValue) {
        super(
                new OtpErlangObject[]{new OtpErlangString("goodName"), new OtpErlangString("startingValue")},
                new OtpErlangObject[]{new OtpErlangString(goodName), new OtpErlangInt(startingValue)}
        );
        this.goodName = goodName;
        this.startingValue = startingValue;
    }

    public String getGoodName() {
        return goodName;
    }

    public int getStartingValue() {
        return startingValue;
    }
}
