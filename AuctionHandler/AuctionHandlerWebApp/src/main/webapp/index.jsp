<html>

<head>
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
<body>

<%--<h2>Auction Handler - Login Page</h2>--%>
<%--<form action="<%=request.getContextPath()%>/LoginServlet" method="post">--%>
<%--    <div>Username</div>--%>
<%--    <input type="text" name="username">--%>
<%--    <br>--%>
<%--    <div>Password</div>--%>
<%--    <input type="password" name="password">--%>
<%--    <br>--%>
<%--    <button type="submit">LOGIN</button>--%>
<%--</form>--%>


<div class="container">
    <h1 class="d-flex justify-content-center my-5">Auction Handler - Login Page</h1>
    <div class="card">
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/LoginServlet" method="post">
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" placeholder="Enter username" aria-describedby="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" placeholder="Enter password" id="exampleInputPassword1" required>
                </div>
                <button type="submit" class="btn btn-primary">Login</button>
            </form>
            <div>Not Registered? Sign up <a href="<%=request.getContextPath()%>/RegistrationServlet">here</a></div>
        </div>
    </div>
</div>


</body>
</html>
