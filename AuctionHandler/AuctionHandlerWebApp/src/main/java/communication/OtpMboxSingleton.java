package communication;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OtpMboxSingleton {
    //create an object of SingleObject
    private static final String node = "client@localhost";
    private String mailbox;
    private static final String cookie = "abcde";

    private OtpMbox otpMbox = null;

    private OtpMboxSingleton(String id) throws IOException{
        mailbox = id;
        OtpNode otpNode = new OtpNode(node, cookie);
        otpMbox =  otpNode.createMbox(mailbox);
    }

    public static OtpMbox getInstance(HttpSession session) {
        OtpMboxSingleton ret = (OtpMboxSingleton) session.getAttribute("otpmbox");
        if (ret == null) {
            try {
                ret = new OtpMboxSingleton(session.getId());
                session.setAttribute("otpmbox", ret);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ret.otpMbox;
    }

}
