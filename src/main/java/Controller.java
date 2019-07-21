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

import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import me.artish1.menu.*;
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

    //JavaFX GUI injected fields
    @FXML
    private JFXListView<AuctionItem> listView;

    @FXML
    private ListView<MenuItem> menuList;

    @FXML
    private HBox btnExit;

    @FXML
    private AnchorPane paneAuc;

    @FXML
    private AnchorPane paneSettings;

    @FXML
    private AnchorPane paneAbout;

    @FXML
    private AnchorPane paneInterests;

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

    @FXML
    private JFXListView<AuctionItem> interestListView;


    public Controller() {
        data = new DataModel();
    }

    private Node[] panes;

    private void init() {

        Callback<ListView<MenuItem>, ListCell<MenuItem>> menuListCellFactory = new Callback<ListView<MenuItem>, ListCell<MenuItem>>() {
            @Override
            public ListCell<MenuItem> call(ListView<MenuItem> param) {
                ListCell<MenuItem> cell = new MenuListCell<>();
                return cell;
            }
        };

        menuList.setCellFactory(menuListCellFactory);


        panes = new Node[]{paneAuc, paneInterests, paneSettings, paneAbout};

        //Load MenuItems
        MenuItem aucItem = new AuctionMenuItem(paneAuc, panes);
        MenuItem interestItem = new InterestMenuItem(paneInterests, panes);
        MenuItem settingsItem = new SettingsMenu(paneSettings, panes);
        MenuItem aboutItem = new AboutMenuItem(paneAbout, panes);

        MenuItem.menus.add(aucItem);
        MenuItem.menus.add(interestItem);
        MenuItem.menus.add(settingsItem);
        MenuItem.menus.add(aboutItem);

        btnExit.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnExit.setStyle("-fx-background-color: #F0503E");
            }
        });

        btnExit.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                btnExit.setStyle("-fx-background-color: #3D4956");
            }
        });

        ObservableList<MenuItem> l = FXCollections.observableArrayList(MenuItem.menus);
        menuList.setItems(l);


        ObservableList<String> interestList = FXCollections.observableList(DataModel.getPriorityWords());
        listViewSettings.setItems(interestList);
        JFXSmoothScroll.smoothScrollingListView(listViewSettings, 0.6);

        Callback<ListView<AuctionItem>, ListCell<AuctionItem>> cellFactory = new Callback<ListView<AuctionItem>, ListCell<AuctionItem>>() {
            @Override
            public ListCell<AuctionItem> call(ListView<AuctionItem> param) {
                ListCell<AuctionItem> cell = new AuctionListCell<>();
                return cell;
            }
        };

        listView.setCellFactory(cellFactory);
        interestListView.setCellFactory(cellFactory);

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

                if (selection == null || selection.isEmpty())
                    return;

                DataModel.removePriorityWord(selection);
                listViewSettings.refresh();

            }
        });


        //Set All panes to invisible except the Auction Pane
        for (Node n : panes) {
            n.setVisible(false);
        }
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


        menuList.getSelectionModel().selectFirst();
        //menuList.applyCss();

        JFXSmoothScroll.smoothScrollingListView(listView, 0.7);

    }


    @Override
    public void run() {
        init();
        loadListings();


        for (Auction auc : data.getListings()) {
            Thread aucThread = new Thread(auc);
            aucThread.start();

        }

        Thread t1 = new Thread(new WaitForLoad());
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //printAuctions();
        findInterests();
        Platform.runLater(() -> loadListingsUI());
        Platform.runLater(() -> refreshListView(comboAuc.getSelectionModel().getSelectedItem()));

        ObservableList<AuctionItem> list = FXCollections.observableList(data.getInterests());
        interestListView.setItems(list);
    }

    /**
     * Loads all the Auctions into the JavaFX ComboBox comboAuc Node
     * and selects the first item
     */
    private void loadListingsUI() {
        ObservableList<Auction> aucss = FXCollections.observableArrayList();
        for (Auction auc : data.getListings()) {
            aucss.add(auc);
        }
        comboAuc.getItems().addAll(aucss);
        comboAuc.getSelectionModel().selectFirst();
    }


    /**
     * Sets the listView JavaFX Node (The Auction's screen ListView)
     * with the AuctionItems of the Auction provided.
     * @param auc The Auction to provide the items from its list to view.
     */
    public void refreshListView(Auction auc) {
        ObservableList<AuctionItem> list = FXCollections.observableList(auc.getItems());
        listView.setItems(list);
    }


    /**
     * Finds different Auctions and loads them into Auction objects
     * and gets stored using DataModel#addListing(Auction auction)
     * Each Auction object contains an AuctionItem list/gallery loaded.
     *
     */
    private void loadListings() {

        List<HtmlElement> items = Scraper.getPage("https://www.bidrl.com/newhome/affiliates/afid/2")
                .getByXPath("//div[@class='aucbox']");
        if (items.isEmpty()) {
            System.out.println("No auction listings have been found.");
            return;
        }


        for (HtmlElement item : items) {
            Auction auc = new Auction();

            for (HtmlElement s : item.getHtmlElementDescendants()) {
                if (s.getAttribute("class").equalsIgnoreCase("auction_link")) {
                    auc.setAuctionLink(s.getAttribute("href"));
                }
                if (s.getAttribute("itemprop").equalsIgnoreCase("name")) {
                    auc.setAuctionTitle(s.asText());
                }
            }
            if (auc.getAuctionLink() != null)
                data.addListing(auc);

        }
    }

    /**
     * Adds a word/phrase to the interest list to search for auction items that contain the sequence
     * and updates the settings list view visuals.
     *
     * @param text the word or sequence of characters to search auction items for.
     */
    private void addInterest(String text) {
        if (text.isEmpty() || text == null)
            return;
        text = text.toLowerCase();
        if (DataModel.getPriorityWords().contains(text))
            return;
        DataModel.addPriorityWord(text);
        listViewSettings.refresh();
    }


    /**
     * Will search for all AuctionItems through all Auctions
     * that contain any of the listed interest words (Priority words)
     * and add them to a seperate list in DataModel (using DataModel#addInterest(AuctionItem))
     */
    public void findInterests() {
        for (Auction auc : data.getListings()) {
            for (AuctionItem item : auc.getItems()) {
                for (String s : DataModel.getPriorityWords()) {
                    if (item.getName().toLowerCase().contains(s.toLowerCase())) {
                        data.addInterest(item);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Displays all auctions and the items in each auction to the console.
     */
    public void printAuctions() {
        System.out.println(data.getListings().size() + " auctions");
        for (Auction auc : data.getListings()) {
            Auction.printAuction(auc);

        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
