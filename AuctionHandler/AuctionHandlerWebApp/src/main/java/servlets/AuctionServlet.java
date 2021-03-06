package servlets;

import com.ericsson.otp.erlang.*;
import communication.CommunicationHandler;
import dto.Auction;
import dto.AuctionState;
import dto.Bid;
import dto.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

@WebServlet(name = "AuctionServlet", value = "/AuctionServlet")
public class AuctionServlet extends HttpServlet {
    public static final int AUCTION_REFRESH_TIME = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AuctionState auctionState = null;

        try {
            auctionState = new CommunicationHandler().getAuctionState(request.getSession());
        } catch (OtpErlangDecodeException | OtpErlangExit | OtpErlangRangeException e) {
            e.printStackTrace();
        }

        if (auctionState != null) {
            System.out.println("Auction state (remaining time: " + auctionState.getRemainingTime() + ")");
            request.getSession().setAttribute("currentAuctionState", auctionState);
        }

        String targetJSP = "/pages/auction.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DoPost Auction");
        AuctionState auctionState = null;
        long bid = Long.parseLong(request.getParameter("bid"));

        try {
            auctionState = new CommunicationHandler().publishBid(request.getSession(), new Bid((String) request.getSession().getAttribute("username"), bid));
        } catch (OtpErlangDecodeException | OtpErlangExit | OtpErlangRangeException e) {
            e.printStackTrace();
        }

        if (auctionState != null) {
            System.out.println("Auction state (remaining time: " + auctionState.getRemainingTime() + ")");
            request.getSession().setAttribute("currentAuctionState", auctionState);
        }

        String targetJSP = "/pages/auction.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }
}
