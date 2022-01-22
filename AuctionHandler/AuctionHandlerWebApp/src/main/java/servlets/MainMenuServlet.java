package servlets;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangRangeException;
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
        } catch (OtpErlangExit | OtpErlangRangeException | OtpErlangDecodeException e) {
            e.printStackTrace();
        }

        String targetJSP = "/pages/main_menu.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doPost MainMenu");
        String targetJSP = "/pages/main_menu.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }
}
