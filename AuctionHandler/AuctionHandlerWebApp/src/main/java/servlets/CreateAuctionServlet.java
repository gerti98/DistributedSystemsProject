package servlets;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangPid;
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
        long duration = Long.parseLong(request.getParameter("duration"));
        long startValue = Long.parseLong(request.getParameter("startValue"));
        String username = (String) request.getSession().getAttribute("username");
        String imageURL = request.getParameter("imageURL");
        Auction auction = new Auction(goodname, duration, startValue, imageURL, username);

        System.out.println("DoPost Auction Creation");
        System.out.println("goodname: " + goodname + "\nstartValue: " + startValue + "\nusername: " + username);

        OtpErlangPid pid = null;
        try {
            pid = new CommunicationHandler().performAuctionCreation(request.getSession(), auction);
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        if (pid != null) {
            System.out.println("Auction creation succeded");
            Auction updatedAuction = new Auction(goodname, duration, startValue, imageURL, username, pid);
            request.getSession().setAttribute("currentAuction", updatedAuction);
            response.sendRedirect(request.getContextPath() + "/AuctionServlet");
        } else {
            System.out.println("Auction creation failed");
            request.getSession().setAttribute("auctionCreationStatus", "error");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/create_auction.jsp");
            requestDispatcher.forward(request, response);
        }
    }
}
