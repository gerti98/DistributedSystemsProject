<%--
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
</head>
<body>
Auction
<div>
  <div>Remaining Time: 00:00:20</div>
  <form action="<%=request.getContextPath()%>/AuctionServlet" method="post">
    <input type="number" name="bid">
    <button type="submit"> BID </button>
  </form>
</div>
</body>
</html>
