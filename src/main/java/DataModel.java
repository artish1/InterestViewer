import me.artish1.auction.Auction;
import me.artish1.auction.AuctionItem;
import me.artish1.web.Browsing;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DataModel {

    private List<Auction> listings = new ArrayList<>();
    private List<AuctionItem> interestedListings = new ArrayList<>();
    private static List<String> priorityWords;

    private static Controller controller;

    public DataModel(Controller contr) {
        controller = contr;
    }

    public static void loadPriorityWords(){
        String[] words = {"drum", "drums", "cpu", "processor", "drive",
                "graphics", "gtx", "geforce", "msi", "motherboard",
                "xbox", "ps4", "switch", "intel", "samsung", "galaxy",
                "computer", "desktop"};

        priorityWords= new LinkedList<String>(Arrays.asList(words));
        //TODO Add editable priority words for client use. (Instead of Hard coding)
    }

    public static List<String> getPriorityWords() {
        return priorityWords;

    }

    public static void addPriorityWord(String interest) {
        priorityWords.add(interest);
        controller.refreshInterestListView();
    }

    public static void removePriorityWord(String interest){
        priorityWords.remove(interest);
        controller.refreshInterestListView();
    }


    public void addListing(Auction auc){
        listings.add(auc);
    }


    public List<AuctionItem> getInterests()
    {
        return interestedListings;
    }

    public void addInterest(AuctionItem item)
    {
        interestedListings.add(item);
    }



    public List<Auction> getListings(){
        return listings;
    }



}
