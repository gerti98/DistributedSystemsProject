package dto;

import com.ericsson.otp.erlang.*;

public class Auction{
    String goodName;
    long duration;
    long startingValue;
    String username;
    String imageURL;
//    OtpErlangPid pid;

    public Auction(String goodName, long duration, long startingValue, String imageURL, String username) {
        this.goodName = goodName;
        this.duration = duration;
        this.startingValue = startingValue;
        this.imageURL = imageURL;
        this.username = username;
    }

//    public Auction(String goodName, long duration, long startingValue, String imageURL, String username, OtpErlangPid pid) {
//        this(goodName, duration, startingValue, imageURL, username);
//        this.pid = pid;
//    }
//    public OtpErlangPid getPid() {
//        return pid;
//    }

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

    public long getDuration() {
        return duration;
    }


    @Override
    public String toString(){
        return "AuctionObject{goodName: " + goodName + ", duration: " + duration +
                ", startingValue: " + startingValue + ", username: " + username +
                ", imageURL: " + imageURL + "}\n";
    }

    public OtpErlangMap encodeInErlangMap(){
        return new OtpErlangMap(
            new OtpErlangObject[]{new OtpErlangString("goodName"), new OtpErlangString("duration"), new OtpErlangString("startingValue"), new OtpErlangString("imageURL"), new OtpErlangString("username")},
            new OtpErlangObject[]{new OtpErlangString(goodName), new OtpErlangLong(duration), new OtpErlangLong(startingValue), new OtpErlangString(imageURL), new OtpErlangString(username)}
        );
    }

    public static Auction decodeFromErlangList(OtpErlangList list){
        String goodname = ((OtpErlangString) list.elementAt(0)).stringValue();
        long duration = ((OtpErlangLong) list.elementAt(1)).longValue();
        long value = ((OtpErlangLong) list.elementAt(2)).longValue();
        String imageURL =  ((OtpErlangString) list.elementAt(3)).stringValue();
        String username = ((OtpErlangString) list.elementAt(4)).stringValue();
        return new Auction(goodname, duration, value, imageURL, username);
    }
}
