package communication;

import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

import java.io.IOException;

public class OtpMboxSingleton {
    //create an object of SingleObject
    private static String node = "client@localhost";
    private static String mailbox = "mbox";
    private static final String cookie = "abcde";


    private static OtpMbox otpMbox = null;


    public static OtpMbox getInstance() {
        if (otpMbox == null) {
            try {
                System.out.println("Generated new instance of otpMbox");
                OtpNode otpNode = new OtpNode(node, cookie);
                otpMbox =  otpNode.createMbox(mailbox);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return otpMbox;
    }

}
