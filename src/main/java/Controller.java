import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.jfoenix.controls.*;
import javafx.application.Platform;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import me.artish1.web.Scraper;
import me.artish1.auction.Auction;
import me.artish1.auction.AuctionItem;
import me.artish1.jfx.JFXSmoothScroll;
import me.artish1.threads.WaitForLoad;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Runnable, Initializable {

    private DataModel data;

    //JavaFX GUI
    @FXML
    private JFXListView<AuctionItem> listView;

    @FXML
    private HBox btnAuc;

    @FXML
    private HBox btnSettings;

    @FXML
    private HBox btnAbout;

    @FXML
    private ListView<String> sampleList;

    @FXML
    private HBox btnExit;

    @FXML
    private AnchorPane paneAuc;

    @FXML
    private AnchorPane paneSettings;

    @FXML
    private AnchorPane paneAbout;

    @FXML
    private JFXComboBox<Auction> comboAuc;

    @FXML
    private JFXListView<String> listViewSettings;

    @FXML
    private JFXButton btnAddInterest;

    @FXML
    private JFXButton btnRemoveInterest;

    @FXML
    private JFXTextField txtViewInterest;




    public Controller() { data = new DataModel(this); }

    private void init()
    {

        ObservableList<String> interestList = FXCollections.observableList(DataModel.getPriorityWords());
        listViewSettings.setItems(interestList);
        JFXSmoothScroll.smoothScrollingListView(listViewSettings, 0.6);
       // ListCell<AuctionItem> cells = listView.getCellFactory().call(listView);

        Callback<ListView<AuctionItem>, ListCell<AuctionItem>> cellFactory = new Callback<ListView<AuctionItem>, ListCell<AuctionItem>>() {
            @Override
            public ListCell<AuctionItem> call(ListView<AuctionItem> param) {
                ListCell<AuctionItem> cell = new AuctionListCell<>();
                return cell;
            }


        };

        listView.setCellFactory(cellFactory);


        txtViewInterest.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addInterest(txtViewInterest.getText());
                txtViewInterest.setText("");
            }
        });


        btnAddInterest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addInterest(txtViewInterest.getText());
                txtViewInterest.setText("");
            }
        });

        btnRemoveInterest.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String selection = listViewSettings.getSelectionModel().getSelectedItem();

                if(selection.isEmpty() || selection == null)
                    return;

                DataModel.removePriorityWord(selection);
                //listViewSettings.getItems().remove(selection);
                listViewSettings.refresh();

            }
        });

        paneAbout.setVisible(false);
        paneSettings.setVisible(false);
        paneAuc.setVisible(true);


        comboAuc.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Auction>() {
            @Override
            public void changed(ObservableValue<? extends Auction> observable, Auction oldValue, Auction newValue) {
                refreshListView(newValue);
            }
        });


        btnExit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Platform.exit();
            }
        });

        btnSettings.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                paneAbout.setVisible(false);
                paneSettings.setVisible(true);
                paneAuc.setVisible(false);
            }
        });

        btnAbout.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                paneAbout.setVisible(true);
                paneSettings.setVisible(false);
                paneAuc.setVisible(false);
            }
        });

        btnAuc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                paneAbout.setVisible(false);
                paneSettings.setVisible(false);
                paneAuc.setVisible(true);
            }
        });
        JFXSmoothScroll.smoothScrollingListView(listView, 0.7);

    }


    @Override public void run() {
        init();
        loadListings();


        for(Auction auc : getListings()){
            Thread aucThread =  new Thread(auc);
            aucThread.start();

        }

        Thread t1 = new Thread(new WaitForLoad());
        t1.start();
        try {
            t1.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        //printAuctions();
        findInterests();
        Platform.runLater(() -> loadListingsUI());
        Platform.runLater(() -> refreshListView(comboAuc.getSelectionModel().getSelectedItem()));

    }


    private void loadListingsUI()
    {
        ObservableList<Auction> aucss = FXCollections.observableArrayList();
        for(Auction auc : data.getListings())
        {
            aucss.add(auc);
        }
        comboAuc.getItems().addAll(aucss);
        comboAuc.getSelectionModel().selectFirst();
    }


    /**
     *
     */
    public void refreshListView(Auction auc){
        ObservableList<AuctionItem> list = FXCollections.observableList(auc.getItems());
        listView.setItems(list);
    }


    public List<Auction> getListings(){
        return data.getListings();
    }

    /**
     * Finds different Auctions and loads them into Auction objects.
     * Each Auction object contains an AuctionItem list/gallery which contains the items
     * for each Auction.
     */
    private void loadListings(){

        List<HtmlElement> items = Scraper.getPage("https://www.bidrl.com/newhome/affiliates/afid/2")
                .getByXPath("//div[@class='aucbox']");
        if(items.isEmpty()){
            System.out.println("No auction listings have been found.");
            return;
        }


        for(HtmlElement item : items) {
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

    private void addInterest(String text){
        if(text.isEmpty() || text == null)
            return;
        text = text.toLowerCase();
        if(DataModel.getPriorityWords().contains(text))
            return;
        DataModel.addPriorityWord(text);
        listViewSettings.refresh();
    }

    private void removeInterest(String text){

    }

    public void findInterests()
    {
        for(Auction auc : data.getListings())
        {
            for(AuctionItem item : auc.getItems())
            {
                for(String s : DataModel.getPriorityWords()) {
                    if (item.getName().toLowerCase().contains(s.toLowerCase())){
                        data.addInterest(item);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Displays all auctions and the items in each auction to the console.
     *
     */
    public void printAuctions(){
        System.out.println(data.getListings().size()  + " auctions");
        for(Auction auc : data.getListings()){
            Auction.printAuction(auc);

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
