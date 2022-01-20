package communication;

import com.ericsson.otp.erlang.*;
import dto.User;

import java.io.IOException;


public class CommunicationHandler {
    private static final String serverNode = "server@localhost";
    private static final String serverPID = "main_server";

    public boolean performSendReply(String operation, User user) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance();
        System.out.println("Created mbox with name: " + otpMbox.getName());

        OtpErlangTuple request = new OtpErlangTuple(new OtpErlangObject[]{otpMbox.self(), new OtpErlangAtom(operation), new OtpErlangString(user.getUsername()), new OtpErlangString(user.getPassword())});
        otpMbox.send(serverPID, serverNode, request);
        System.out.println("Sent request " + operation + " for user " + user.getUsername() + " at server " + serverPID);

        OtpErlangObject message = otpMbox.receive();

        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            status = (OtpErlangAtom) ((OtpErlangTuple) message).elementAt(1);
            System.out.println("Received message content: {" + serverPID.toString() + ", " + status.toString() + "}");
        }

        return status.toString().equals("ok");

    }
    public boolean performUserSignUp(User user) throws IOException, OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignUp");
        return performSendReply("register_user", user);
    }

    public boolean performUserLogIn(User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignIn");
        return performSendReply("login_user", user);
    }
}
