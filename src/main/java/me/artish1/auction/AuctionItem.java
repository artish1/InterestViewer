package me.artish1.auction;

public class AuctionItem {
    private String name, itemLink, lotNumber, imageUrl;
    private String currentBid, highestBidder;

    public AuctionItem(String name, String itemLink, String lotNumber, String imageUrl,
                       String currentBid, String highestBidder){
        this.name = name;
        this.itemLink =  "https://www.bidrl.com/" + itemLink;
        this.lotNumber = lotNumber;
        this.imageUrl = imageUrl;
        this.currentBid = currentBid;
        setHighestBidder(highestBidder);
    }


    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder.substring(3);
    }

    public String getCurrentBid() {
        return currentBid;
    }

    public String getHighestBidder() {
        return highestBidder;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getItemLink() {
        return itemLink;
    }

    public String getLotNumber() {
        return lotNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() { return this.getName(); }
}
