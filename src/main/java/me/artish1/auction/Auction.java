package me.artish1.auction;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import me.artish1.web.Scraper;

import java.util.ArrayList;
import java.util.List;

public class Auction implements Runnable {
    private String auctionLink, auctionTitle;
    private List<AuctionItem> items = new ArrayList<>();

    private static final Object lock = new Object();
    private static int loading = 0;

    public List<AuctionItem> getItems() {
        return items;
    }

    public String getAuctionTitle() {
        return auctionTitle;
    }


    private static void addLoading(int i) {
        synchronized (lock) {
            loading += i;
        }
    }

    private static void subtractLoading(int i) {
        synchronized (lock) {
            loading -= i;
        }
    }

    /**
     * returns the amount of how many auctions are still downloading its AuctionItems into memory.
     *
     * @return the amount of Auctions downloading AuctionItems
     */
    public static int getLoading() {
        synchronized (lock) {
            return loading;
        }
    }


    @Override
    public void run() {
        addLoading(1);
        loadItemListings();
        subtractLoading(1);
    }

    /**
     * Downloads the html page and looks through each item in the listing of the auction
     * and loads appropriately.
     *
     * @throws NullPointerException
     */
    public void loadItemListings() throws NullPointerException {
        if (auctionLink != null) {
            HtmlPage elementList = Scraper.getPage(auctionLink);
            List<HtmlElement> asd = elementList.getByXPath("//div[@itemprop='offers']");

            for (HtmlElement item : asd) {
                String itemUrl = "", lotNumber = "", itemName = "", highestBidder = "", currentBid = "", imageUrl = "";
                for (HtmlElement data : item.getHtmlElementDescendants()) {
                    if (data.getAttribute("itemprop").equalsIgnoreCase("url"))
                        itemUrl = data.getAttribute("href");

                    if (data.getTagName().equalsIgnoreCase("sup"))
                        lotNumber = data.asText();

                    if (data.getAttribute("itemprop").equalsIgnoreCase("image"))
                        imageUrl = data.getAttribute("src");

                    if (data.getAttribute("itemprop").equalsIgnoreCase("name"))
                        itemName = data.asText();
                    if (data.getAttribute("class").equalsIgnoreCase("wbid ")) {
                        highestBidder = data.asText();
                    }
                    if (data.getAttribute("class").equalsIgnoreCase("cbid")) {
                        currentBid = data.asText();
                    }
                }
                items.add(new AuctionItem(itemName, itemUrl, lotNumber, imageUrl, currentBid, highestBidder));

            }

        } else throw new NullPointerException("Auction link is null.");
    }


    /**
     * Prints only the auction provided in the parameter
     * @param auc The Auction to print out to the console.
     */
    public static void printAuction(Auction auc) {
        System.out.println("============================Auction: " + auc.getAuctionTitle() + "============================");

        for (AuctionItem item : auc.getItems()) {

            System.out.println("--" + item.getLotNumber() + ":");
            System.out.println("----Name: " + item.getName());
            System.out.println("----URL: " + item.getItemLink());
            System.out.println("----Highest Bidder: " + item.getHighestBidder());
            System.out.println("----Current Bid: " + item.getCurrentBid());
            System.out.println("----Image: " + item.getImageUrl());
        }
    }

    public void setAuctionTitle(String auctionTitle) {
        this.auctionTitle = auctionTitle;
    }

    public String getAuctionLink() {
        return auctionLink;
    }

    public void setAuctionLink(String auctionLink) {
        this.auctionLink = auctionLink;
    }

    @Override
    public String toString() {
        return auctionTitle;
    }

}
