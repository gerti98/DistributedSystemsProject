package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "CheckSelectedAuctionFilter", urlPatterns = {"/AuctionServlet"})
public class AuctionSelectedFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws ServletException, IOException {
        System.out.println("AuctionSelectedFilter");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);
        String auctionURI = request.getContextPath() + "/AuctionServlet";
        String menuAuctionURI = request.getContextPath() + "/MainMenuServlet";

        boolean isAuctionSelected = session != null && session.getAttribute("currentAuction") != null;

        if (isAuctionSelected) {
            chain.doFilter(request, response);
        } else {
            response.sendRedirect(menuAuctionURI);
        }
    }

    @Override
    public void destroy() {
    }
}
