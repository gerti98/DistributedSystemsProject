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
    <title>Registration</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</head>

<div class="container">
    <h1 class="d-flex justify-content-center my-5">Auction Handler - Register Page</h1>
    <div class="card">
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/RegistrationServlet" method="post"
                  oninput='confirmPassword.setCustomValidity(confirmPassword.value != password.value ? "Passwords do not match." : "")'>
                <div class="mb-3">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" class="form-control" name="username" placeholder="Enter username" aria-describedby="username" id="username" required>
                </div>
                <div class="mb-3">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" class="form-control" name="password" placeholder="Enter password" id="password" required>
                </div>
                <div class="mb-3">
                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                    <input type="password" class="form-control" name="confirmPassword" placeholder="Enter password" id="confirmPassword">
                </div>
                <%
                    String registrationStatus = (String) request.getSession().getAttribute("registrationStatus");
                    if(registrationStatus != null && registrationStatus.equals("error")) {
                %>
                <div class="alert alert-danger" role="alert">
                    There was an unexpected error, please retry later.
                </div>
                <%
                    }
                %>
                <button type="submit" class="btn btn-primary">Register</button>
            </form>
            <div>Already Registered? Sign in <a href="<%=request.getContextPath()%>/LoginServlet">here</a></div>
        </div>
    </div>
</div>

</html>
