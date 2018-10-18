import me.artish1.WebScraper.Scraper;
import me.artish1.auction.Auction;

import java.util.ArrayList;
import java.util.List;

public class DataModel {

    private List<Auction> listings = new ArrayList<>();
    private Controller controller;

    public DataModel(Controller controller){
        this.controller = controller;
    }


    public void addListing(Auction auc){
        listings.add(auc);
    }

    public List<Auction> getListings(){
        return listings;
    }



}
