<%--
  Created by IntelliJ IDEA.
  User: gxhan
  Date: 19/01/2022
  Time: 22:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Auction</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.min.js" integrity="sha384-QJHtvGhmr9XOIpI6YVutG+2QOK9T+ZnN4kzFN1RtK3zEFEIsxhlmWl5/YESvpZ13" crossorigin="anonymous"></script>
</head>
<body>

<div class="container">
    <div class="d-flex p-3">
        <a href="<%=request.getContextPath()%>/MainMenuServlet" class="btn btn-danger">Back</a>
    </div>
    <div class="card">

        <h3 class="d-flex justify-content-center p-3">
            Create Auction
        </h3>
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/CreateAuctionServlet" method="post" id="create_auction_form">
                <div class="mb-3">
                    <label for="goodname" class="form-label">Good Name</label>
                    <input type="text" class="form-control" name="goodname" placeholder="Enter Good Name" aria-describedby="goodname" id="goodname" required>
                </div>
                <div class="mb-3">
                    <label for="duration" class="form-label">Auction Duration</label>
                    <div class="input-group">
                        <input type="number" class="form-control" name="duration" placeholder="Enter the Auction duration in seconds" id="duration" required>
                        <div class="input-group-append">
                            <span class="input-group-text">s</span>
                        </div>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="startValue" class="form-label">Start Value</label>
                    <div class="input-group">
                        <div class="input-group-prepend">
                            <span class="input-group-text">€</span>
                        </div>
                        <input type="number" class="form-control" name="startValue" placeholder="Enter starting value" id="startValue" required>
                        <div class="input-group-append">
                            <span class="input-group-text">.00</span>
                        </div>
                    </div>
                </div>

                <div class="mb-3">
                    <label for="imageURL" class="form-label">Image URL</label>
                    <input type="text" class="form-control" name="imageURL" placeholder="Enter URL of symbolic image" aria-describedby="imageURL" id="imageURL" required>
                </div>
                <%
                    String registrationStatus = (String) request.getSession().getAttribute("auctionCreationStatus");
                    if(registrationStatus != null && registrationStatus.equals("error")) {
                %>
                <div class="alert alert-danger" role="alert">
                    There was an unexpected error, please retry later.
                </div>
                <%
                    }
                %>
                <button type="submit" class="btn btn-primary m-3">Create Auction</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
