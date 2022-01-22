package servlets;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import communication.CommunicationHandler;
import dto.Auction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CreateAuctionServlet", value = "/CreateAuctionServlet")
public class CreateAuctionServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetJSP = "/pages/create_auction.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodname = request.getParameter("goodname");
        int startValue = Integer.parseInt(request.getParameter("startValue"));
        String username = (String) request.getSession().getAttribute("username");
        String imageURL = request.getParameter("imageURL");

        System.out.println("DoPost Auction Creation");
        System.out.println("goodname: " + goodname + "\nstartValue: " + startValue + "\nusername: " + username);

        CommunicationHandler communicationHandler = new CommunicationHandler();
        boolean isAuctionCreationOkay = false;
        try {
            isAuctionCreationOkay = communicationHandler.performAuctionCreation(request.getSession(), new Auction(goodname, startValue, imageURL, username));
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        if (isAuctionCreationOkay) {
            //TODO change session
            System.out.println("Auction creation succeded");
            response.sendRedirect(request.getContextPath() + "/AuctionServlet");
        } else {
            System.out.println("Auction creation failed");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/create_auction.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
