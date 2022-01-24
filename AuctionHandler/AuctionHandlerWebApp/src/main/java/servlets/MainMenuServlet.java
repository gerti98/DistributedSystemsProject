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
        response.setIntHeader("Refresh", MAIN_MENU_REFRESH_TIME);

        try {
            List<Auction> auctionList = new CommunicationHandler().fetchActiveAuctions(request.getSession());
            request.setAttribute("auctionList", auctionList);
            request.getSession().setAttribute("auctionList", auctionList);
        } catch (OtpErlangExit | OtpErlangRangeException | OtpErlangDecodeException e) {
            e.printStackTrace();
        }

        RequestDispatcher requestDispatcher = request.getRequestDispatcher( "/pages/main_menu.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost MainMenu");
        int index = Integer.parseInt(request.getParameter("id"));
        System.out.println("Id: " + index);
        List<Auction> auctionList = (List<Auction>) request.getSession().getAttribute("auctionList");
        Auction selectedAuction = auctionList.get(index);
        System.out.println("auction: goodname: " + selectedAuction.getGoodName() + ", startingvalue: " + selectedAuction.getStartingValue() + ", username: " + selectedAuction.getUsername() + ", imageURL: " + selectedAuction.getImageURL() + ", pidauctionhandler: " + selectedAuction.getPid());

        //TODO: add new_user message
        request.getSession().setAttribute("currentAuction", selectedAuction);
        response.sendRedirect(request.getContextPath() + "/AuctionServlet");
    }
}
