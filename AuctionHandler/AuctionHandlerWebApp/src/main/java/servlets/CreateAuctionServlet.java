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
        request.getSession().removeAttribute("auctionCreationStatus");
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodname = request.getParameter("goodname");
        int startValue = Integer.parseInt(request.getParameter("startValue"));
        String username = (String) request.getSession().getAttribute("username");
        String imageURL = request.getParameter("imageURL");
        Auction auction = new Auction(goodname, startValue, imageURL, username);

        System.out.println("DoPost Auction Creation");
        System.out.println("goodname: " + goodname + "\nstartValue: " + startValue + "\nusername: " + username);

        boolean isAuctionCreationOkay = false;
        try {
            isAuctionCreationOkay = new CommunicationHandler().performAuctionCreation(request.getSession(), auction);
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        if (isAuctionCreationOkay) {
            System.out.println("Auction creation succeded");
            request.getSession().setAttribute("currentAuction", auction);
            response.sendRedirect(request.getContextPath() + "/AuctionServlet");
        } else {
            System.out.println("Auction creation failed");
            request.getSession().setAttribute("auctionCreationStatus", "error");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/create_auction.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
