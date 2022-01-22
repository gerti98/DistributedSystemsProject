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
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setIntHeader("Refresh", 1);
        String targetJSP = "/pages/auction.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetJSP;
        String goodname = request.getParameter("goodname");
        int startValue = Integer.parseInt(request.getParameter("startValue"));


        System.out.println("DoPost Auction Creation");
        System.out.println("goodname: " + goodname + "\nstartValue: " + startValue);

        CommunicationHandler communicationHandler = new CommunicationHandler();
        boolean isAuctionCreationOkay = false;
        try {
            isAuctionCreationOkay = communicationHandler.performAuctionCreation(request.getSession(), new Auction(goodname, startValue));
        } catch (OtpErlangDecodeException e) {
            e.printStackTrace();
        } catch (OtpErlangExit e) {
            e.printStackTrace();
        }


        if (isAuctionCreationOkay) {
            //TODO change session
            System.out.println("Auction creation succeded");
            targetJSP = "/pages/auction.jsp";
        } else {
            System.out.println("Auction creation failed");
            targetJSP = "/pages/create_auction.jsp";
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }
}
