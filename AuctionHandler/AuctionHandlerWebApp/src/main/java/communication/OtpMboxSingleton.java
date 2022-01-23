package communication;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import javax.servlet.http.HttpSession;
import java.io.IOException;

public class OtpMboxSingleton {
    private static final String cookie = "abcde";

    private OtpMbox otpMbox = null;

    private OtpMboxSingleton(String id) throws IOException{
        OtpNode otpNode = new OtpNode(id + "@localhost", cookie);
        otpMbox =  otpNode.createMbox(id);
        System.out.println("OtpMBOX name: " + otpMbox.getName() + ", otpMBOX pid" + otpMbox.self().toString());
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
