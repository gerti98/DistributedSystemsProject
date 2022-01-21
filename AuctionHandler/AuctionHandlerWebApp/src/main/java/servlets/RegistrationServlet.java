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

@WebServlet(name = "RegistrationServlet", value = "/RegistrationServlet")
public class RegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("doGet");
        String targetJSP = "/pages/registration.jsp";
        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String targetJSP;
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println("DoPost Registration");
        System.out.println("username: " + username + "\npassword: " + password);

        CommunicationHandler communicationHandler = new CommunicationHandler();
        boolean isSignUpOkay = false;
        try {
            isSignUpOkay = communicationHandler.performUserSignUp(request.getSession(), new User(username, password));
        } catch (OtpErlangDecodeException e) {
            e.printStackTrace();
        } catch (OtpErlangExit e) {
            e.printStackTrace();
        }

        if (isSignUpOkay){
            System.out.println("Sign up succeeded");
            targetJSP = "/index.jsp";
        }
        else
            targetJSP = "/pages/registration.jsp";

        RequestDispatcher requestDispatcher = request.getRequestDispatcher(targetJSP);
        requestDispatcher.forward(request, response);
    }
}
