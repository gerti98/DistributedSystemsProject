package dto;

import java.util.ArrayList;


public class AuctionList {
    ArrayList<Auction> auctionList;
    boolean active;

    public AuctionList(ArrayList<Auction> auctionList, boolean active) {
        this.auctionList = auctionList;
        this.active = active;
    }

    public ArrayList<Auction> getAuctionList() {
        return auctionList;
    }

    public boolean isActive() {
        return active;
    }
}
