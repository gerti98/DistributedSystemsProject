let ws;

function connect(ctx, username) {
    let host = document.location.host;
    const url = "ws://" +host  + ctx + "/main_menu_endpoint/" + username;
    console.log("Connecting to main menu endpoint with url: " + url);
    ws = new WebSocket(url);

    ws.onmessage = function(event) {
        //Logic to remove message
        console.log("Arrived new auction list")
        var auctionListObject = JSON.parse(event.data);
        console.log(auctionListObject);
        console.log(auctionListObject.auctionList);
        console.log(auctionListObject.active)
        updateAuctionList(ctx, auctionListObject.auctionList, auctionListObject.active);
    };
}

function show_auctions(active){
    var active_card = document.querySelector("#active_auction_card")
    var past_card = document.querySelector("#past_auction_card")

    if(active == true){
        active_card.classList.remove("d-none")
        past_card.classList.add("d-none")
    } else {
        active_card.classList.add("d-none")
        past_card.classList.remove("d-none")
    }
}

/*
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
 */
function updateAuctionList(ctx, auctionList, active){
    var parentNode;
    if(active)
        parentNode = document.querySelector('#parent_auction_list');
    else
        parentNode = document.querySelector('#parent_auction_list_finished')

    while (parentNode.firstChild) { parentNode.removeChild(parentNode.firstChild); }

    auctionList.forEach(
        auction => {
            console.log("Single auction: ");
            console.log(auction)
            form = createCard(ctx, auction, active);
            parentNode.append(form)
        }
    )
}

function createCard(ctx, message, active){
    const form = document.createElement("form");
    form.classList.add("card",  "w-25")
    form.setAttribute("action", ctx + "/MainMenuServlet");
    form.setAttribute("method", "post");

    const image = document.createElement("img");
    image.classList.add("card-img-top")
    image.setAttribute("src", message.goodName);
    image.setAttribute("onError", "this.onerror=null;this.src='" + ctx + "/resources/default-placeholder.png';" );
    image.setAttribute("alt",message.goodName + " image")


    const main_div = document.createElement("div");
    main_div.classList.add("card-body",  "d-flex",  "flex-column", "justify-content-between", "p-3")

    const mid_div = document.createElement("div");

    const goodname = document.createElement("input");
    goodname.setAttribute("type", "hidden");
    goodname.setAttribute("name", "goodname");
    goodname.value = message.goodName;

    const duration = document.createElement("input");
    duration.value = message.duration;
    duration.setAttribute("type", "hidden");
    duration.setAttribute("name", "duration");


    const startingValue = document.createElement("input");
    startingValue.value = message.startingValue;
    startingValue.setAttribute("type", "hidden");
    startingValue.setAttribute("name", "startingValue");


    const username = document.createElement("input");
    username.value = message.username;
    username.setAttribute("type", "hidden");
    username.setAttribute("name", "username");


    const imageURL = document.createElement("input");
    imageURL.value = message.imageURL;
    imageURL.setAttribute("type", "hidden");
    imageURL.setAttribute("name", "imageURL");

    const title = document.createElement("h5");
    title.classList.add("card-title");
    title.innerHTML = message.goodName

    const from_div = document.createElement("div");
    from_div.innerHTML = "From: " + message.startingValue

    const created_by_div = document.createElement("div");
    created_by_div.innerHTML = "Created by: " + message.username

    var last_elem;
    if(active){
        last_elem = document.createElement("button");
        last_elem.classList.add("btn", "btn-primary", "m-3")
        last_elem.innerHTML = "Enter"
    } else {
        last_elem = document.createElement("div");
        last_elem.classList.add("d-flex", "justify-content-center", "p-3")
        last_elem.innerHTML = "Winner: " + message.winner
    }


    mid_div.append(goodname, duration, startingValue, username, imageURL, title, from_div, created_by_div)
    main_div.append(mid_div, last_elem)
    form.append(image, main_div)
    return form
}