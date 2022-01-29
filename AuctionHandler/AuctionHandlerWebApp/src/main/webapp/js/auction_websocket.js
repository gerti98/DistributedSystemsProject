let ws;

function connect_to_auction_ws(ctx, username, auction) {
    console.log("Connect");
    let host = document.location.host;
    const url = "ws://" +host  + ctx + "/auction_state/" + auction + "/"  + username;
    console.log("Connecting to auction state endpoint with url: " + url);
    ws = new WebSocket(url);

    ws.onmessage = function(event) {
        //Logic to remove message
        console.log("Arrived new auction state")
        console.log(event.data);
        var auctionState = JSON.parse(event.data);
        console.log(auctionState);
        updateAuctionState(ctx, username,  auctionState);
    };

    startTimer();
}

function updateAuctionState(ctx, curr_username, auctionState){
    const joiner_users_node = document.querySelector('#joined_users');
    const bids_node = document.querySelector('#bids');

    while (joiner_users_node.firstChild) { joiner_users_node.removeChild(joiner_users_node.firstChild); }
    while (bids_node.firstChild) { bids_node.removeChild(bids_node.firstChild); }

    remainingTime = auctionState.remainingTime;
    const div = document.querySelector("#remainingTime")
    div.innerHTML = remainingTime

    console.log("Remaining time: " + remainingTime)
    userlist = auctionState.participants;
    userlist.forEach(
        user => {
            console.log("Current user: " + user.username)
            const li =  document.createElement("li");
            if(user.username == curr_username)
                li.classList.add("list-group-item", "active");
            else
                li.classList.add("list-group-item");
            li.innerHTML = user.username
            joiner_users_node.append(li);
        }
    )

    offerslist = auctionState.offers;

    offerslist.forEach(
        offer => {
            console.log("Current offer: User " + offer.username + " bid " + offer.bid)
            const li =  document.createElement("li");
            li.classList.add("list-group-item");
            li.innerHTML = offer.username + " bids " + offer.bid + "€"
            bids_node.append(li);
        }
    )
    console.log("updateAuctionState")

    if(auctionState.winner_elected){
        console.log("Winner was elected!");
        const winner_bid = auctionState.winning_bid;
        const modal_body = document.querySelector('#modal_body');
        modal_body.innerHTML = "The winner is " + winner_bid.username + " with a bid of " + winner_bid.bid + "€"
        $('#exampleModal').modal({backdrop: 'static', keyboard: false});
        $('#exampleModal').modal('show');
    }
}

function startTimer(duration, display) {
    var duration =  document.querySelector('#remainingTime').textContent;
    console.log(duration, typeof duration);
    display = document.querySelector('#time_formatted');
    if (duration < 0)
        duration = 0;
    var timer = duration, hours, minutes, seconds;
    setInterval(function () {
        hours = parseInt(timer / 3600, 10);
        minutes = parseInt((timer / 60) % 60, 10);
        seconds = parseInt(timer % 60, 10);

        hours = hours < 10 ? "0" + hours : hours;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = hours + ":" + minutes + ":" + seconds;
        if(--timer < 0)
            timer=0;
    }, 1000);
}
