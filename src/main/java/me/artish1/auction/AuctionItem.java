package me.artish1.auction;

public class AuctionItem {
    String name, itemLink, lotNumber, imageUrl;
    String currentBid, highestBidder;

    public AuctionItem(String name, String itemLink, String lotNumber, String imageUrl,
                       String currentBid, String highestBidder){
        this.name = name;
        this.itemLink =  "https://www.bidrl.com/" + itemLink;
        this.lotNumber = lotNumber;
        this.imageUrl = imageUrl;
        this.currentBid = currentBid;
        this.highestBidder = highestBidder;
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

}
