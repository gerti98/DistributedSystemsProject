package communication;

import com.ericsson.otp.erlang.*;
import dto.Auction;
import dto.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


public class CommunicationHandler {
    private static final String serverNode = "server@localhost";
    private static final String serverPID = "main_server_endpoint";
    private static final int receiveTimeoutMS = 5000;
    private static final int receiveFetchMS = 100;

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
        send(s, new OtpErlangAtom("register"), user);
        return receiveRequestResult(s);

    }

    public boolean performUserLogIn(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignIn");
        send(s, new OtpErlangAtom("login"), user);
        return receiveRequestResult(s);
    }

    public boolean performAuctionCreation(HttpSession s, Auction auction) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform Auction creation");
        send(s, new OtpErlangAtom("create_auction"), auction);
        return receiveRequestResult(s);
    }

    public List<Auction> fetchActiveAuctions(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to fetch Active Auctions");
        send(s, new OtpErlangAtom("get_active_auctions"));
        return receiveFetchAuctionResult(s);
    }

    public void send(HttpSession session, OtpErlangObject... values){
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Created mbox with name: " + otpMbox.getName());

        OtpErlangObject[] arr = new OtpErlangObject[values.length + 1];

        arr[0] = otpMbox.self();
        System.arraycopy(values, 0, arr, 1, values.length);

        OtpErlangTuple request = new OtpErlangTuple(arr);
        otpMbox.send(serverPID, serverNode, request);
        System.out.println("Sent request " + request + " at server " + serverPID);

    }

    public boolean receiveRequestResult(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveTimeoutMS);
        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            status = (OtpErlangAtom) (resulTuple).elementAt(0);
        }
        return status.toString().equals("ok");
    }


    private List<Auction> receiveFetchAuctionResult(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        List<Auction> auctionList = new ArrayList<>();
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveFetchMS);
        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            status = (OtpErlangAtom) (resulTuple).elementAt(0);
            OtpErlangList resultList = (OtpErlangList) (resulTuple).elementAt(1);

            for(OtpErlangObject result : resultList){
                OtpErlangList list = (OtpErlangList) result;
                String goodname = ((OtpErlangString) list.elementAt(0)).stringValue();
                long value = ((OtpErlangLong) list.elementAt(1)).longValue();
                String imageURL =  ((OtpErlangString) list.elementAt(2)).stringValue();
                String username = ((OtpErlangString) list.elementAt(3)).stringValue();
                OtpErlangPid pid = ((OtpErlangPid) list.elementAt(4));

                System.out.println("fetched auction (goodname: "+ goodname + ", startingValue:" + value + ", imageURL:" + imageURL + ", username:" + username + ", pid: " + pid.toString() + ")");
                auctionList.add(new Auction(goodname, value, imageURL, username, pid));
            }
        }
        return auctionList;
    }
}
