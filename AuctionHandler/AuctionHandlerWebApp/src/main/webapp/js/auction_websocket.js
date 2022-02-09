let ws;


function check_minimun_bid(){
    var min_value_to_show;
    bid = document.querySelector('#bid');
    minimum_bid = document.querySelector('#minimum_bid');
    last_bid_elem = document.querySelector('#bids').firstElementChild;

    bid_value_int = parseInt(bid.value)
    minimum_bid_int = parseInt(minimum_bid.value)

    if(last_bid_elem != null){
        last_bid_value = parseSentenceForNumber(last_bid_elem.innerHTML)
        last_bid_value_int = parseInt(last_bid_value)
        // console.log("Last bid value: ", last_bid_value, typeof last_bid_value)
        // console.log("Last bid value (int): ", last_bid_value_int, typeof  last_bid_value_int)
        min_value_to_show = Math.max(last_bid_value_int, minimum_bid_int)
        check = bid_value_int < min_value_to_show
    } else {
        min_value_to_show = minimum_bid.value

    }

    // console.log("Bid: ", bid.value, typeof bid.value)
    // console.log("Bid(int): ", bid_value_int, typeof  bid_value_int)
    // console.log("Minimum_bid: ", minimum_bid.value, typeof minimum_bid.value)
    // console.log("Minimum_bid(int): ", minimum_bid_int, typeof  minimum_bid_int)
    // console.log("Last_bid_elem: ", last_bid_elem)


    var check = bid_value_int < min_value_to_show
    bid.setCustomValidity((check)? "Minimum offer must be " + min_value_to_show + "€" : "")
}

function parseSentenceForNumber(sentence){
    var matches = sentence.replace(/,/g, '').match(/(\+|-)?((\d+(\.\d+)?)|(\.\d+))/);
    return matches && matches[0] || null;
}

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
        console.log("Winner is: ", winner_bid.username, typeof winner_bid.username)
        if (winner_bid.username == "NoWinner"){
            modal_body.innerHTML = "No Winner in this auction"
        } else {
            modal_body.innerHTML = "The winner is " + winner_bid.username + " with a bid of " + winner_bid.bid + "€"
        }
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
        console.log("tick")
        if(--timer < 0)
            timer=0;

        hours = parseInt(timer / 3600, 10);
        minutes = parseInt((timer / 60) % 60, 10);
        seconds = parseInt(timer % 60, 10);

        hours = hours < 10 ? "0" + hours : hours;
        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = hours + ":" + minutes + ":" + seconds;
    }, 1000);
}
