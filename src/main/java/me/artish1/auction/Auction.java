package me.artish1.auction;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import me.artish1.WebScraper.Scraper;

import java.util.ArrayList;
import java.util.List;

public class Auction {
    private String auctionLink, auctionTitle;
    private List<AuctionItem> items = new ArrayList<>();



    public void loadPageData() throws NullPointerException {
           if(auctionLink != null) {
               HtmlPage elementList = Scraper.getPage(auctionLink);
               List<HtmlElement> asd = elementList.getByXPath("//div[@itemprop='offers']");

               for(HtmlElement item : asd){
                   String itemUrl = "", lotNumber = "", itemName = "", highestBidder = "", currentBid = "", imageUrl = "";
                   for(HtmlElement data : item.getHtmlElementDescendants()){

                        if(data.getAttribute("itemprop").equalsIgnoreCase("url"))
                            itemUrl = data.getAttribute("href");

                        if(data.getTagName().equalsIgnoreCase("sup"))
                            lotNumber = data.asText();

                        if(data.getAttribute("itemprop").equalsIgnoreCase("image"))
                            imageUrl = data.getAttribute("src");

                        if(data.getAttribute("itemprop").equalsIgnoreCase("name"))
                            itemName = data.asText();
                        if(data.getAttribute("class").equalsIgnoreCase("wbid"))
                            highestBidder = data.asText();
                        if(data.getAttribute("class").equalsIgnoreCase("cbid"))
                            currentBid = data.asText();

                   }
                   items.add(new AuctionItem(itemName, itemUrl, lotNumber, imageUrl, currentBid, highestBidder));

               }

           }
            else throw new NullPointerException("Auction link is null.");
    }

    public List<AuctionItem> getItems() {
        return items;
    }

    public String getAuctionTitle() {
        return auctionTitle;
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
    public String toString(){
        return auctionTitle + ": " + auctionLink;
    }

}
