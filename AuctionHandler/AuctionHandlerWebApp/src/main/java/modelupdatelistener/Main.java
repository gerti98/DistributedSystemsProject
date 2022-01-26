package modelupdatelistener;

import com.ericsson.otp.erlang.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int MAX_T = 3;
    private static final String node_name = "listener@localhost";
    private static final String cookie = "abcde";
    private static final String mailbox = "mbox";

    public static void main(String[] args) throws IOException, OtpErlangDecodeException, OtpErlangExit, URISyntaxException {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        OtpNode otpNode = new OtpNode(node_name, cookie);
        OtpMbox otpMbox = otpNode.createMbox(mailbox);

        while(true) {
            System.out.println("Receiving...");
            OtpErlangObject message = otpMbox.receive();
            Runnable r = new MessageTask(message);
            pool.execute(r);
        }
    }
}
