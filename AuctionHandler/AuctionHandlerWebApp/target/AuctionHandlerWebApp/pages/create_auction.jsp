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
    <h1>Auction Handler - Create Auction</h1>
    <div class="card">
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/AuctionServlet" method="post">

                <div class="form-group row">
                    <label for="goodName" class="col-sm-2 col-form-label">Good Name</label>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="goodName"
                               placeholder="Enter Good name">
                    </div>
                </div>

                <div class="form-group row">
                    <label for="value" class="col-sm-2 col-form-label">Starting Value</label>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="value"
                               placeholder="Enter Starting Value">
                    </div>
                </div>

                <div class=" form-group row">
                    <label for="imageURL" class="col-sm-2 col-form-label">Image URL</label>
                    <div class="col-sm-7">
                        <input type="text" class="form-control" name="username"
                               placeholder="Enter image URL">
                    </div>
                </div>


                <button type="submit" class="btn btn-primary">Complete Create Auction</button>
            </form>
        </div>
    </div>
</div>
</body>
</html>
