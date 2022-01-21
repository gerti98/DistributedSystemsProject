package communication;

import com.ericsson.otp.erlang.*;
import dto.User;

import javax.servlet.http.HttpSession;
import java.io.IOException;


public class CommunicationHandler {
    private static final String serverNode = "server@localhost";
    private static final String serverPID = "main_server_endpoint";
    private static final int receiveTimeoutMS = 5000;

    /*public boolean performSendReply(String operation, User user) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance();
        System.out.println("Created mbox with name: " + otpMbox.getName());

        OtpErlangTuple request = new OtpErlangTuple(new OtpErlangObject[]{otpMbox.self(), new OtpErlangAtom(operation), new OtpErlangString(user.getUsername()), new OtpErlangString(user.getPassword())});
        otpMbox.send(serverPID, serverNode, request);
        System.out.println("Sent request " + operation + " for user " + user.getUsername() + " at server " + serverPID);

        OtpErlangObject message = otpMbox.receive(5000);

        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            status = (OtpErlangAtom) ((OtpErlangTuple) message).elementAt(1);
            System.out.println("Received message content: {" + serverPID.toString() + ", " + status.toString() + "}");
        }

        return status.toString().equals("ok");
    }*/

    public boolean performUserSignUp(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignUp");
        return send(s, new OtpErlangAtom("register"), user);
    }

    public boolean performUserLogIn(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignIn");
        return send(s, new OtpErlangAtom("login"), user);
    }

    public boolean send(HttpSession session, OtpErlangObject... values) throws OtpErlangDecodeException, OtpErlangExit{

        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Created mbox with name: " + otpMbox.getName());

        OtpErlangObject[] arr = new OtpErlangObject[values.length + 1];

        arr[0] = otpMbox.self();
        System.arraycopy(values, 0, arr, 1, values.length);

        OtpErlangTuple request = new OtpErlangTuple(arr);
        otpMbox.send(serverPID, serverNode, request);

        System.out.println("Sent request " + request + " at server " + serverPID);

        OtpErlangObject message = otpMbox.receive(receiveTimeoutMS);

        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            status = (OtpErlangAtom) ((OtpErlangTuple) message).elementAt(1);
            System.out.println("Received message content: {" + serverPID.toString() + ", " + status.toString() + "}");
        }

        return status.toString().equals("ok");
    }
}
