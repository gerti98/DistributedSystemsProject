package dto;

import com.ericsson.otp.erlang.*;

public class Auction extends OtpErlangMap{
    String goodName;
    long startingValue;
    String username;
    String imageURL;
    OtpErlangPid pid;

    public Auction(String goodName, long startingValue, String imageURL, String username) {
        super(
                new OtpErlangObject[]{new OtpErlangString("goodName"), new OtpErlangString("startingValue"), new OtpErlangString("imageURL"), new OtpErlangString("username")},
                new OtpErlangObject[]{new OtpErlangString(goodName), new OtpErlangLong(startingValue), new OtpErlangString(imageURL), new OtpErlangString(username)}
        );
        this.goodName = goodName;
        this.startingValue = startingValue;
        this.imageURL = imageURL;
        this.username = username;
    }

    public Auction(String goodName, long startingValue, String imageURL, String username, OtpErlangPid pid) {
        this(goodName, startingValue, imageURL, username);
        this.pid = pid;
    }



    public String getGoodName() {
        return goodName;
    }

    public long getStartingValue() {
        return startingValue;
    }

    public String getUsername() {
        return username;
    }


    public String getImageURL() {
        return imageURL;
    }

    public OtpErlangPid getPid() {
        return pid;
    }

}
