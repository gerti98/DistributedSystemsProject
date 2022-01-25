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
        send(s, serverRegisteredPID, new OtpErlangAtom("register"), user);
        return receiveRequestResult(s);
    }

    public boolean performUserLogIn(HttpSession s, User user) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform User SignIn");
        send(s, serverRegisteredPID, new OtpErlangAtom("login"), user);
        return receiveRequestResult(s);
    }

    public OtpErlangPid performAuctionCreation(HttpSession s, Auction auction) throws OtpErlangDecodeException, OtpErlangExit {
        System.out.println("Trying to perform Auction creation");
        send(s, serverRegisteredPID, new OtpErlangAtom("create_auction"), auction);
        return receiveAuctionPid(s);
    }

    public AuctionState getAuctionState(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction creation");
        Auction auction = (Auction) s.getAttribute("currentAuction");
        sendToPid(s, auction.getPid(), new OtpErlangAtom("get_auction_state"));
        return receiveAuctionState(s);
    }

    public boolean performAuctionJoin(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction creation");
        Auction auction = (Auction) s.getAttribute("currentAuction");
        sendToPid(s, auction.getPid(), new OtpErlangAtom("new_user"), new User((String) s.getAttribute("username")));
        return receiveRequestResult(s);
    }
    public AuctionState publishBid(HttpSession s, Bid bid) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to perform Auction creation");
        Auction auction = (Auction) s.getAttribute("currentAuction");
        sendToPid(s, auction.getPid(), new OtpErlangAtom("new_offer"), bid);
        return receiveAuctionState(s);
    }

    public List<Auction> fetchActiveAuctions(HttpSession s) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        System.out.println("Trying to fetch Active Auctions");
        send(s, serverRegisteredPID, new OtpErlangAtom("get_active_auctions"));
        return receiveFetchAuctionResult(s);
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
        System.out.println("AuctionHandler PID info: (id: " + auctionHandlerPID.id() + ", node: " + auctionHandlerPID.node() + ", serial: " + auctionHandlerPID.serial() + "creation: " + auctionHandlerPID.creation());
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

    public OtpErlangPid receiveAuctionPid(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit {
        OtpErlangAtom status = new OtpErlangAtom("");
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveTimeoutMS);
        if(message instanceof OtpErlangTuple){
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            status = (OtpErlangAtom) (resulTuple).elementAt(0);
            OtpErlangPid pid = (OtpErlangPid) (resulTuple).elementAt(1);
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
                OtpErlangList list = (OtpErlangList) result;
                String goodname = ((OtpErlangString) list.elementAt(0)).stringValue();
                long duration = ((OtpErlangLong) list.elementAt(1)).longValue();
                long value = ((OtpErlangLong) list.elementAt(2)).longValue();
                String imageURL =  ((OtpErlangString) list.elementAt(3)).stringValue();
                String username = ((OtpErlangString) list.elementAt(4)).stringValue();
                OtpErlangPid pid = ((OtpErlangPid) list.elementAt(5));
                System.out.println("fetched auction (goodname: "+ goodname + ", duration: " + duration + ",startingValue:" + value + ", imageURL:" + imageURL + ", username:" + username + ", pid: " + pid.toString() + ")");
                auctionList.add(new Auction(goodname, duration, value, imageURL, username, pid));
            }
        }
        return auctionList;
    }


    private AuctionState receiveAuctionState(HttpSession session) throws OtpErlangDecodeException, OtpErlangExit, OtpErlangRangeException {
        OtpMbox otpMbox = OtpMboxSingleton.getInstance(session);
        OtpErlangObject message = otpMbox.receive(receiveFetchMS);
        long remainingTime = 0;
        List<String> userList = new ArrayList<>();
        List<List<String>> offers = new ArrayList<>();

        if(message instanceof OtpErlangTuple) {
            OtpErlangPid serverPID = (OtpErlangPid) ((OtpErlangTuple) message).elementAt(0);
            OtpErlangTuple resulTuple = (OtpErlangTuple) ((OtpErlangTuple) message).elementAt(1);
            remainingTime = ((OtpErlangLong) (resulTuple).elementAt(1)).longValue();
            OtpErlangList userListOtp = (OtpErlangList) ((resulTuple).elementAt(2));
            for (OtpErlangObject userOtp: userListOtp){
                String username = ((OtpErlangString) userOtp).stringValue();
                System.out.println("Fetched from state user: " + username);
                if (!username.equals(session.getAttribute("username")))
                    userList.add(username);
            }

            OtpErlangList offersListOtp = (OtpErlangList) ((resulTuple).elementAt(3));
            for(OtpErlangObject offerOtp: offersListOtp){
                OtpErlangList tempListOtp = (OtpErlangList) offerOtp;
                List<String> tempList = new ArrayList<>();
                String username = ((OtpErlangString) tempListOtp.elementAt(0)).stringValue();
                long amount = ((OtpErlangLong) tempListOtp.elementAt(1)).longValue();
                tempList.add(username);
                tempList.add(Long.toString(amount));
                System.out.println("Fetched from offers (username: " + username + ", amount: " + amount + ")");
                offers.add(tempList);
            }
        }

        return new AuctionState(remainingTime, userList, offers);
    }



}
