import com.gargoylesoftware.htmlunit.html.HtmlElement;
import me.artish1.WebScraper.Scraper;
import me.artish1.auction.Auction;
import me.artish1.auction.AuctionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller implements Runnable{

    private DataModel data;
    private View view;
    private static List<String> priorityWords = new ArrayList<>();

    public static void loadPriorityWords(){
        String[] words = {"drum", "cpu", "processor", "drive", "graphics", "gtx", "geforce", "msi", "motherboard", ""};
        Arrays.asList(words);

    }


    public static void main(String args[]){

        System.out.println("Hello world.");
        Controller controller = new Controller();
        Thread t1 = new Thread(controller);
        t1.start();
        try{
            t1.join();
        }catch(InterruptedException ex){
            ex.printStackTrace();
        }
        for(Auction auc : controller.data.getListings()){
            auc.loadPageData();
        }
        printAuctions(controller);




    }

    public static void printAuctions(Controller controller){
        for(Auction auc : controller.data.getListings()){
            System.out.println("-----------------Auction: " + auc.getAuctionTitle() + "--------------");

            for(AuctionItem item : auc.getItems()){

                System.out.println("--" + item.getLotNumber()+ ":");
                System.out.println("----Name: " + item.getName());
                System.out.println("----URL: " + item.getItemLink());
                System.out.println("----Highest Bidder: " + item.getHighestBidder());
                System.out.println("----Current Bid: " + item.getCurrentBid());
                System.out.println("----Image: " + item.getImageUrl());

            }

        }
    }


    public Controller(){
        data = new DataModel(this);


    }

    @Override
    public void run() {
        printListings();
    }



    public void printListings(){

        List<HtmlElement> items = Scraper.getPage("https://www.bidrl.com/newhome/affiliates/afid/2")
                .getByXPath("//div[@class='aucbox']");
        if(items.isEmpty()){
            System.out.println("No auction listings have been found.");
            return;
        }


        for(HtmlElement item : items) {
            //System.out.println("Item: " + item);

            Auction auc = new Auction();

            for (HtmlElement s : item.getHtmlElementDescendants()) {
                //System.out.println(s.getAttribute("href"));
                if (s.getAttribute("class").equalsIgnoreCase("auction_link")) {
                    auc.setAuctionLink(s.getAttribute("href"));
                }
                if(s.getAttribute("itemprop").equalsIgnoreCase("name")){
                    auc.setAuctionTitle(s.asText());
                }

            }

                    if(auc.getAuctionLink() != null)
                        data.addListing(auc);

        }
    }





}
