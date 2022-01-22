package servlets;

import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import communication.CommunicationHandler;
import dto.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet Login");

        //Remove logged username session attribute (i.e. cause the Logout button was pressed)
        request.getSession().removeAttribute("username");
        String targetJSP = "/index.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetJSP;
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("DoPost Login");
        System.out.println("username: " + username + "\npassword: " + password);

        CommunicationHandler communicationHandler = new CommunicationHandler();
        boolean isLoginOkay = false;
        try {
            isLoginOkay = communicationHandler.performUserLogIn(request.getSession(), new User(username, password));
        } catch (OtpErlangDecodeException | OtpErlangExit e) {
            e.printStackTrace();
        }

        if (isLoginOkay) {
            request.getSession().setAttribute("username", username);
            System.out.println("Sign up succeded");
            response.sendRedirect(request.getContextPath() + "/MainMenuServlet");
        } else {
            System.out.println("Sign in failed");
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/index.jsp");
            requestDispatcher.forward(request, response);
        }

    }
}
