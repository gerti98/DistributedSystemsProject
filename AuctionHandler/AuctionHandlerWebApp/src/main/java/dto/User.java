package dto;

import com.ericsson.otp.erlang.OtpErlangLong;
import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class User{
    String username;
    String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString(){
        return "BidObject{username: " + username + ", bid: " + password + "}\n";
    }

    public OtpErlangMap encodeInErlangMap(){
        return new OtpErlangMap(
                new OtpErlangObject[]{new OtpErlangString("username"), new OtpErlangString("password")},
                new OtpErlangObject[]{new OtpErlangString(username), new OtpErlangString(password)}
        );
    }
}
