<%@ page import="dto.Auction" %>
<%@ page import="java.util.List" %>
<%@ page import="java.io.OutputStream" %><%--
  Created by IntelliJ IDEA.
  User: gxhan
  Date: 19/01/2022
  Time: 21:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main Menu</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/main_menu_websocket.js"></script>
</head>
<body onload="connect('<%=request.getContextPath()%>', '<%=request.getSession().getAttribute("username")%>');">
    <div class="container">
        <div class="d-flex d-flex justify-content-between p-3">
            <a href="<%=request.getContextPath()%>/LoginServlet" class="btn btn-danger">Logout</a>
            <h4 id="username"> Username: <%=request.getSession().getAttribute("username")%></h4>
            <a href="<%=request.getContextPath()%>/CreateAuctionServlet" class="btn btn-primary">CreateAuction</a>
        </div>
        <div class="card">
            <h3 class="d-flex justify-content-center p-3">
                Available Auctions
            </h3>
            <div class="p-4 d-flex flex-wrap" id="parent_auction_list">
                    <%
                        List<Auction> auctionList = (List<Auction>) request.getAttribute("auctionList");
                        if(auctionList == null || auctionList.size() == 0){
                    %>
                        <h5 class="d-flex justify-content-center p-3" id="noauction">Nothing to Show<h5>
                    <%
                        } else {
                            for(int i=0; i<auctionList.size(); i++){
                                Auction auction = auctionList.get(i);
                        %>
                            <form class="card w-25" action="<%=request.getContextPath()%>/MainMenuServlet" method="post">
                                <img class="card-img-top" src="<%=auction.getImageURL()%>"  onError="this.onerror=null;this.src='<%=request.getContextPath()%>/resources/default-placeholder.png';" alt="<%=auction.getGoodName()%> image">
                                <div class="card-body d-flex flex-column justify-content-between p-3">
                                    <div>
                                        <input type="hidden" name="goodname" value="<%=auction.getGoodName()%>">
                                        <input type="hidden" name="duration" value="<%=auction.getDuration()%>">
                                        <input type="hidden" name="startingValue" value="<%=auction.getStartingValue()%>">
                                        <input type="hidden" name="imageURL" value="<%=auction.getImageURL()%>">
                                        <input type="hidden" name="username" value="<%=auction.getUsername()%>">
                                        <h5 class="card-title"><%=auction.getGoodName()%></h5>
                                        <div>From: <%=auction.getStartingValue()%>€</div>
                                        <div>Created By: <%=auction.getUsername()%></div>
                                    </div>
                                    <button type="submit" class="btn btn-primary m-3">Enter</button>
                                </div>
                            </form>
                        <%
                            }
                        }
                    %>
            </div>
        </div>
    </div>
</body>
</html>
