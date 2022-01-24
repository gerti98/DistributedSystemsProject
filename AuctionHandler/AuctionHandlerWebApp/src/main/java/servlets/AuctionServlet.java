package servlets;

import com.ericsson.otp.erlang.*;
import communication.CommunicationHandler;
import dto.Auction;
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
        response.setIntHeader("Refresh", AUCTION_REFRESH_TIME);

        //Communication with auction handler erlang process
        try {
            new CommunicationHandler().pingAuctionHandler(request.getSession());
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        String targetJSP = "/pages/auction.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DoPost Auction");
    }
}
