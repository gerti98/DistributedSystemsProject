package communication;

import com.ericsson.otp.erlang.*;
import dto.Auction;
import dto.AuctionState;
import dto.Bid;
import dto.User;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


public class CommunicationHandler {
    private static final String serverNode = "server@localhost";
    private static final String serverRegisteredPID = "main_server_endpoint";
    private static final int receiveTimeoutMS = 5000;
    private static final int receiveFetchMS = 100;

    public boolean performUserSignUp(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignUp");
        send(s, serverRegisteredPID, new OtpErlangAtom("register"), user.encodeInErlangMap());
        return receiveRequestResult(s);
    }

    public boolean performUserLogIn(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignIn");
        send(s, serverRegisteredPID, new OtpErlangAtom("login"), user.encodeInErlangMap());
        return receiveRequestResult(s);
    }

    public OtpErlangPid performAuctionCreation(HttpSession s, Auction auction) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform Auction creation");
        send(s, serverRegisteredPID, new OtpErlangAtom("create_auction"), auction.encodeInErlangMap());
        return receiveAuctionPid(s);
    }

    public AuctionState getAuctionState(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction creation");
        OtpErlangPid pid = (OtpErlangPid) s.getAttribute("currentAuctionPid");
        sendToPid(s, pid, new OtpErlangAtom("get_auction_state"));
        return receiveAuctionState(s);
    }

    public boolean performAuctionJoin(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction joining");
        OtpErlangPid pid = (OtpErlangPid) s.getAttribute("currentAuctionPid");
        User user = new User((String) s.getAttribute("username"), "pippo");
        sendToPid(s, pid, new OtpErlangAtom("new_user"), user.encodeInErlangMap());
        return receiveRequestResult(s);
    }

    public boolean performAuctionExit(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction joining");
        OtpErlangPid pid = (OtpErlangPid) s.getAttribute("currentAuctionPid");
        User user = new User((String) s.getAttribute("username"), "pippo");
        sendToPid(s, pid, new OtpErlangAtom("del_user"), user.encodeInErlangMap());
        return receiveRequestResult(s);
    }

    public AuctionState publishBid(HttpSession s, Bid bid) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction creation");
        OtpErlangPid pid = (OtpErlangPid) s.getAttribute("currentAuctionPid");
        sendToPid(s, pid, new OtpErlangAtom("new_offer"), bid.encodeInErlangMap());
        return receiveAuctionState(s);
    }

    public List<Auction> fetchActiveAuctions(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to fetch Active Auctions");
        send(s, serverRegisteredPID, new OtpErlangAtom("get_active_auctions"));
        return receiveFetchAuctionResult(s);
    }

    public List<Auction> fetchPastAuctions(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to fetch Past Auctions");
        send(s, serverRegisteredPID, new OtpErlangAtom("get_passed_auctions"));
        return receiveFetchAuctionResult(s);
    }

    public OtpErlangPid getAuctionPid(HttpSession session, Auction selectedAuction) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to get auction pid");
        send(session, serverRegisteredPID, new OtpErlangAtom("get_auction_pid"), selectedAuction.encodeInErlangMap());
        return receiveAuctionPid(session);
    }

    public void send(HttpSession session, String serverRegisteredPID, OtpErlangObject... values){
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Created mbox with name: " + otpMbox.getName());
        OtpErlangObject[] arr = new OtpErlangObject[values.length + 1];
        arr[0] = otpMbox.self();
        System.arraycopy(values, 0, arr, 1, values.length);
        OtpErlangTuple request = new OtpErlangTuple(arr);
        otpMbox.send(serverRegisteredPID, serverNode, request);
        System.out.println("Sent request " + request + " at server " + serverRegisteredPID);
    }

    public void sendToPid(HttpSession session, OtpErlangPid auctionHandlerPID, OtpErlangObject... values){
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Created mbox with name: " + otpMbox.getName());
        OtpErlangObject[] arr = new OtpErlangObject[values.length + 1];
        arr[0] = otpMbox.self();
        System.arraycopy(values, 0, arr, 1, values.length);
        OtpErlangTuple request = new OtpErlangTuple(arr);
        otpMbox.send(auctionHandlerPID, request);
        System.out.println("Sent request " + request + " at server " + auctionHandlerPID.toString());
    }

    public void sendToPidTest(HttpSession session, OtpErlangPid auctionHandlerPID, OtpErlangAtom atom, OtpErlangMap username) {
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Created mbox with name: " + otpMbox.getName());
        OtpErlangObject[] arr = new OtpErlangObject[]{otpMbox.self(), atom, username};
        OtpErlangTuple request = new OtpErlangTuple(arr);
        otpMbox.send(auctionHandlerPID, request);
        System.out.println("Sent request " + request + " at server " + auctionHandlerPID.toString());
    }

    public boolean receiveRequestResult(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        System.out.println("Receiving request result... ");
        OtpErlangObject message = otpMbox.receive(receiveTimeoutMS);
        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            status = (OtpErlangAtom) (resulTuple).elementAt(0);
        }
        return status.toString().equals("ok");
    }

    public OtpErlangPid receiveAuctionPid(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveTimeoutMS);
        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            status = (OtpErlangAtom) (resulTuple).elementAt(0);
            if (status.toString().equals("false"))
                return null;
            OtpErlangList list = (OtpErlangList) (resulTuple).elementAt(1);
            OtpErlangPid pid = (OtpErlangPid) (list).elementAt(0);
            return pid;
        }
        return null;
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
                Auction auction = Auction.decodeFromErlangList((OtpErlangList) result);
                System.out.println("Fetched: " + auction);
                auctionList.add(auction);
            }
        }
        return auctionList;
    }


    private AuctionState receiveAuctionState(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveFetchMS);
        AuctionState auctionState = null;

        if(message instanceof OtpErlangTuple) {
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resultTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            auctionState = AuctionState.decodeFromErlangTuple(resultTuple);
        }

        return auctionState;
    }



}
