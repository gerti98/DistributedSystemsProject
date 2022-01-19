<%--
  Created by IntelliJ IDEA.
  User: gxhan
  Date: 19/01/2022
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Registration was performed</title>
</head>
<h2>Auction Handler - Register Page</h2>
<form action="<%=request.getContextPath()%>/RegistrationServlet" method="post">
    <div>Username</div>
    <input type="text" name="username">
    <br>
    <div>Password</div>
    <input type="password" name="password">
    <br>
    <button type="submit">REGISTER</button>
</form>

<div>Already Registered? Sign in <a href="<%=request.getContextPath()%>/LoginServlet">here</a></div>
</html>
