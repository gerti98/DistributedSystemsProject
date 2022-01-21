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
    <title>Title</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</head>
<body>


<%--<h2>Auction Handler - Create Auction</h2>--%>
<%--<form method="post">--%>
<%--    <div>Good Name</div>--%>
<%--    <input type="text">--%>
<%--    <div>Starting Value</div>--%>
<%--    <input type="number">--%>
<%--    <div>Image URL</div>--%>
<%--    <input type="text">--%>

<%--    <button type="submit"> Complete Auction Creation</button>--%>
<%--</form>--%>
<%--<a href="<%=request.getContextPath()%>/MainMenuServlet">Back</a>--%>
<%--<a href="<%=request.getContextPath()%>/AuctionServlet">Complete Auction Creation</a>--%>

<div class="container">
    <div class="d-flex p-3">
        <a href="<%=request.getContextPath()%>/MainMenuServlet" class="btn btn-danger">Back</a>
    </div>
    <div class="card">

        <h3 class="d-flex justify-content-center p-3">
            Create Auction
        </h3>
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/AuctionServlet" method="post">
                <div class="mb-3">
                    <label for="goodname" class="form-label">Good Name</label>
                    <input type="text" class="form-control" name="goodname" placeholder="Enter Good Name" aria-describedby="goodname">
                </div>
                <div class="mb-3">
                    <label for="startValue" class="form-label">Start Value</label>
                    <input type="number" class="form-control" name="startValue" placeholder="Enter starting value" id="startValue">
                </div>
                <button type="submit" class="btn btn-primary">Create Auction</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
