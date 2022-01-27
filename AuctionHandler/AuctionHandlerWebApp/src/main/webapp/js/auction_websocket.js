let ws;

function connect_to_auction_ws(ctx, username, auction) {
    console.log("Connect");
    let host = document.location.host;
    const url = "ws://" +host  + ctx + "/auction_state/" + auction + "/"  + username;
    ws = new WebSocket(url);

    ws.onmessage = function(event) {
        //Logic to remove message
        console.log("Arrived new auction state")
        console.log(event.data);
        var auctionState = JSON.parse(event.data);
        console.log(auctionState);
        updateAuctionList(ctx, auctionState);
    };
}

function updateAuctionState(ctx, auctionList){
    console.log("updateAuctionState")
}