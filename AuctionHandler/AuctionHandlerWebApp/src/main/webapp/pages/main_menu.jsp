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
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/main_menu_websocket.js"></script>
</head>
<body onload="connect('<%=request.getContextPath()%>', '<%=request.getSession().getAttribute("username")%>');">


    <div class="container">
        <div class="d-flex d-flex justify-content-between p-3">
            <a href="<%=request.getContextPath()%>/LoginServlet" class="btn btn-danger">Logout</a>
            <h4 id="username"> Username: <%=request.getSession().getAttribute("username")%></h4>
            <a href="<%=request.getContextPath()%>/CreateAuctionServlet" class="btn btn-primary">CreateAuction</a>
        </div>
        <div class="d-flex d-flex justify-content-center p-3">
            <div class="btn-group" role="group" aria-label="Basic radio toggle button group">
                <input type="radio" class="btn-check" onclick="show_auctions(true)" name="btnradio" id="btnradio1" autocomplete="off" checked>
                <label class="btn btn-outline-primary" for="btnradio1">ONGOING AUCTIONS</label>

                <input type="radio" class="btn-check" onclick="show_auctions(false)" name="btnradio" id="btnradio2" autocomplete="off">
                <label class="btn btn-outline-primary" for="btnradio2">PAST AUCTIONS</label>
            </div>
        </div>

        <div class="card" id="active_auction_card">
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
        <div class="card d-none" id="past_auction_card">
            <h3 class="d-flex justify-content-center p-3">
                Past Auctions
            </h3>
            <div class="p-4 d-flex flex-wrap" id="parent_auction_list_finished">
                <%
                    List<Auction> pastAuctionList = (List<Auction>) request.getAttribute("pastAuctionList");
                    if(pastAuctionList == null || pastAuctionList.size() == 0){
                %>
                <h5 class="d-flex justify-content-center p-3" id="noauction">Nothing to Show<h5>
                        <%
                        } else {
                            for(int i=0; i<pastAuctionList.size(); i++){
                                Auction auction = pastAuctionList.get(i);
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
                            <div class="d-flex justify-content-center p-3">Winner: <%=auction.getWinner()%></div>
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
