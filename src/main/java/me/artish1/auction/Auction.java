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
            int pageAmount = 1;

            HtmlPage page = Scraper.getPage(auctionLink + "/page/1");

            //Look for how many pages there are in an auction listing.-------
            List<HtmlElement> pageElements = page.getByXPath("//fieldset[@class='pager']/div");

            if (!pageElements.isEmpty()) {
                String pagesString = pageElements.get(0).asText().trim();
                pagesString = pagesString.substring(pagesString.length() - 1);
                try {
                    pageAmount = Integer.parseInt(pagesString);
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
            //---------------------------------------------------------------

            //Re-use the HtmlPage object instead of making another one from looping
            //for better performance.
            loadItems(page);
            for (int i = 2; i <= pageAmount; i++) {
                loadItemsFromPage(i);
                System.out.println("Loading items from page " + i + " from a total of " + pageAmount + " pages.");
            }

        } else throw new NullPointerException("Auction link is null.");
    }

    /**
     * Loads the auction items from the XML/HTML Page
     *
     * @param page The page to retrieve the information from.
     */
    private void loadItems(HtmlPage page) {
        List<HtmlElement> elementList = page.getByXPath("//div[@itemprop='offers']");

        for (HtmlElement item : elementList) {
            String itemUrl, lotNumber, itemName, highestBidder, currentBid, imageUrl;
            //Initialize all the Strings
            itemUrl = lotNumber = itemName = highestBidder = currentBid = imageUrl = "";
            for (HtmlElement data : item.getHtmlElementDescendants()) {

                String itemAttribute = data.getAttribute("itemprop");
                String classAttribute = data.getAttribute("class");
                switch (itemAttribute) {
                    case "url":
                        itemUrl = data.getAttribute("href");

                        break;
                    case "image":
                        imageUrl = data.getAttribute("src");
                        break;
                    case "name":
                        itemName = data.asText();
                        break;
                }

                switch (classAttribute) {
                    case "wbid ":
                        highestBidder = data.asText();
                        break;
                    case "cbid":
                        currentBid = data.asText();
                        break;
                }
                if (data.getTagName().equalsIgnoreCase("sup"))
                    lotNumber = data.asText();

            }
            items.add(new AuctionItem(itemName, itemUrl, lotNumber, imageUrl, currentBid, highestBidder));
        }
    }


    /**
     * Loads the items from a specified page number
     *
     * @param pageNumber The page number to go to load the auction items from.
     */
    private void loadItemsFromPage(int pageNumber) {
        HtmlPage page = Scraper.getPage(auctionLink + "/page/" + pageNumber);
        loadItems(page);
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
