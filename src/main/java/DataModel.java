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

    public static void main(String[] args)
    {

        try {
            URL url = new URL("http://www.google.com");

            Browsing.openWebpage(url);
        }catch(MalformedURLException e) {
            e.printStackTrace();
        }



    }



    private Controller controller;

    public DataModel(Controller controller){
        this.controller = controller;
    }


    public static void loadPriorityWords(){
        String[] words = {"drum", "drums", "cpu", "processor", "drive", "graphics", "gtx", "geforce", "msi", "motherboard", "xbox", "ps4", "usb", "computer", "desktop"};

        priorityWords= new LinkedList<String>(Arrays.asList(words));
        //TODO Add editable priority words for client use. (Instead of Hard coding
    }

    public static List<String> getPriorityWords() {
        return priorityWords;
    }

    public static void addPriorityWord(String interest) {
        priorityWords.add(interest);
    }

    public static void removePriorityWord(String interest){
        if(priorityWords.contains(interest)){
            priorityWords.remove(interest);
        }
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
