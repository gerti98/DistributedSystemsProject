package dto;

import com.ericsson.otp.erlang.OtpErlangMap;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangString;

public class User extends OtpErlangMap {
    String username;
    String password;

    public User(String username, String password) {
        super(
                new OtpErlangObject[]{new OtpErlangString("username"), new OtpErlangString("password")},
                new OtpErlangObject[]{new OtpErlangString(username), new OtpErlangString(password)}
        );
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        super(
                new OtpErlangObject[]{new OtpErlangString("username")},
                new OtpErlangObject[]{new OtpErlangString(username)}
        );
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
