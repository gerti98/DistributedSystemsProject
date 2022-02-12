package servlets;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangPid;
import com.ericsson.otp.erlang.OtpErlangRangeException;
import communication.CommunicationHandler;
import dto.Auction;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "MainMenuServlet", value = "/MainMenuServlet")
public class MainMenuServlet extends HttpServlet {
    //In seconds
    public static final int MAIN_MENU_REFRESH_TIME = 10;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("DoGet MainMenuServlet");

        //Clean joined auction

        if(request.getSession().getAttribute("currentAuction") != null){
            boolean isExitingOkay;
            System.out.println("current Auction is not null, sending exit message");
            try {
                new CommunicationHandler().performAuctionExit(request.getSession());
            } catch (OtpErlangDecodeException | OtpErlangExit | OtpErlangRangeException e) {
                e.printStackTrace();
            }
            request.getSession().removeAttribute("currentAuction");
        }

        try {
            List<Auction> auctionList = new CommunicationHandler().fetchActiveAuctions(request.getSession());
            request.setAttribute("auctionList", auctionList);
            request.getSession().setAttribute("auctionList", auctionList);
            List<Auction> pastAuctionList = new CommunicationHandler().fetchPastAuctions(request.getSession());
            request.setAttribute("pastAuctionList", pastAuctionList);
            request.getSession().setAttribute("pastAuctionList", pastAuctionList);
        } catch (OtpErlangExit | OtpErlangRangeException | OtpErlangDecodeException e) {
            e.printStackTrace();
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/main_menu.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost MainMenu");
        boolean isJoiningOkay = true;
        OtpErlangPid pid = null;

        CommunicationHandler communicationHandler = new CommunicationHandler();
        String goodname = request.getParameter("goodname");
        long duration = Long.parseLong(request.getParameter("duration"));
        long startValue = Long.parseLong(request.getParameter("startingValue"));
        String username = (String) request.getParameter("username");
        String imageURL = request.getParameter("imageURL");
        Auction selectedAuction = new Auction(goodname, duration, startValue, imageURL, username);
        System.out.println("Selected auction: " + selectedAuction);
        try {
            pid = communicationHandler.getAuctionPid(request.getSession(), selectedAuction);
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        if(pid != null){
            System.out.println("Auction pid got: " + pid.toString());
            request.getSession().setAttribute("currentAuction", selectedAuction);
            request.getSession().setAttribute("currentAuctionPid", pid);
            System.out.println("auction: goodname: " + selectedAuction.getGoodName() + ", startingvalue: " + selectedAuction.getStartingValue() + ", username: " + selectedAuction.getUsername() + ", imageURL: " + selectedAuction.getImageURL() + ")");

            try {
                isJoiningOkay = new CommunicationHandler().performAuctionJoin(request.getSession());
            } catch (OtpErlangDecodeException | OtpErlangExit | OtpErlangRangeException e) {
                e.printStackTrace();
            }

            if(isJoiningOkay){
                System.out.println("Joining is going on");
                response.sendRedirect(request.getContextPath() + "/AuctionServlet");
            } else {
                request.getSession().removeAttribute("currentAuction");
                RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/main_menu.jsp");
                requestDispatcher.forward(request, response);
            }
        }


    }
}
