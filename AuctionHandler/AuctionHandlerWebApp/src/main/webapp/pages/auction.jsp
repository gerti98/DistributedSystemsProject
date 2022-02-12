<%@ page import="dto.Auction" %>
<%@ page import="dto.AuctionState" %>
<%@ page import="java.util.List" %>
<%@ page import="dto.Bid" %>
<%@ page import="dto.User" %><%--
  Created by IntelliJ IDEA.
  User: gxhan
  Date: 19/01/2022
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Auction</title>
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
  <script type="text/javascript" src="<%=request.getContextPath()%>/js/auction_websocket.js"></script>

</head>
<%
  Auction auction = (Auction) request.getSession().getAttribute("currentAuction");
  AuctionState auctionState = (AuctionState) request.getSession().getAttribute("currentAuctionState");
  String curr_username = (String) request.getSession().getAttribute("username");
%>
<body onload="connect_to_auction_ws('<%=request.getContextPath()%>', '<%=request.getSession().getAttribute("username")%>', '<%=auction.getGoodName()%>');">

<div class="container">
  <div class="d-flex p-3">
    <a href="<%=request.getContextPath()%>/MainMenuServlet" class="btn btn-danger">Back</a>
  </div>

  <div class="modal fade" id="exampleModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="exampleModalLabel">Auction Result</h5>
        </div>
        <div class="modal-body" id="modal_body">
        </div>
        <div class="modal-footer">
          <a href="<%=request.getContextPath()%>/MainMenuServlet" class="btn btn-danger">Exit Auction</a>
        </div>
      </div>
    </div>
  </div>

  <div class="card my-4">
    <h1 class="d-flex justify-content-center m-3">
      Auction by: <%=auction.getUsername()%>
    </h1>
    <div class="d-flex justify-content-between p-3">

      <div class="d-flex flex-column justify-content-around" style="width: 30%;">
        <div class="d-flex justify-content-center">
          <h3><%=auction.getGoodName()%></h3>
        </div>
        <img class="card-img-top" src="<%=auction.getImageURL()%>"  onError="this.onerror=null;this.src='<%=request.getContextPath()%>/resources/default-placeholder.png';" alt="<%=auction.getGoodName()%> image">

        <h5 class="d-flex justify-content-center">
          <div id="totalTime" style="display: none;"> <%=auction.getDuration()%></div>
          <div id="remainingTime" style="display: none;"><%=auctionState.getRemainingTime()%></div>
          <div>Remaining Time: <b id="time_formatted"><%=auctionState.getFormattedTime()%></b></div>


        </h5>
        <form action="<%=request.getContextPath()%>/AuctionServlet" method="post"
              oninput='check_minimun_bid()'>
          <div class="d-flex justify-content-between mb-3">

            <input type="number" id="minimum_bid" name="minimum_bid" value="<%=auction.getStartingValue()%>" hidden>
            <div class="input-group">
              <div class="input-group-prepend">
                <span class="input-group-text">€</span>
              </div>
              <input type="number" class="form-control" id="bid" name="bid" placeholder="Enter your bid" aria-describedby="bid" required>
              <div class="input-group-append">
                <span class="input-group-text">.00</span>
              </div>
            </div>
            <button type="submit" class="btn btn-primary mx-2 px-4" <%=(auction.getUsername().equals(request.getSession().getAttribute("username"))) ? "disabled" : ""%>> BID </button>
          </div>
        </form>
      </div>
      <div class="d-flex flex-column justify-content-between">
        <div class="my-3">
          <h5 class="d-flex justify-content-center m-2">
            Joined Users
          </h5>
          <ul class="list-group overflow-auto" style="height: 15rem;" id="joined_users">
            <%
              for(User participant: auctionState.getParticipants()){
                if(participant.getUsername().equals(curr_username)){
              %>
                  <li class="list-group-item active"> <%=participant.getUsername()%></li>
              <%
                  } else {
              %>
                    <li class="list-group-item"> <%=participant.getUsername()%></li>
              <%
                  }
              }
            %>
          </ul>
        </div>
        <div  class="my-3">
          <h5 class="d-flex justify-content-center m-2">
            Auction History
          </h5>
          <ul class="list-group overflow-auto" style="height: 15rem;" id="bids">
            <%
              for(Bid bid: auctionState.getOffers()){
            %>
              <li class="list-group-item"> <%=bid.getUsername()%> bids <%=bid.getBid()%>€</li>
            <%
              }
            %>
          </ul>
        </div>
      </div>
    </div>

  </div>
</div>
</body>
</html>
