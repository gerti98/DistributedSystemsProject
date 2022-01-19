<html>
<body>

<h2>Auction Handler - Login Page</h2>
<form action="<%=request.getContextPath()%>/LoginServlet" method="post">
    <div>Username</div>
    <input type="text" name="username">
    <br>
    <div>Password</div>
    <input type="password" name="password">
    <br>
    <button type="submit">LOGIN</button>
</form>

<div>Not Registered? Sign up <a href="<%=request.getContextPath()%>/RegistrationServlet">here</a></div>
</body>
</html>
