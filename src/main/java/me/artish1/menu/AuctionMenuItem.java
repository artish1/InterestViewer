package me.artish1.menu;

import javafx.scene.Node;

public class AuctionMenuItem extends MenuItem {
    private Node screen;
    private Node[] screensOff;

    public AuctionMenuItem(Node screenToChangeTo, Node... screensOff) {
        super("Auctions", "/auction.png", screenToChangeTo, screensOff);
        this.screen = screenToChangeTo;
        this.screensOff = screensOff;
        setStyleColor("#87CEFA");
    }

    @Override
    public void onClick(Node n) {
        super.onClick(n);

        setStyleOnHover(getStyleColor());
        n.setStyle("-fx-background-color:" + getStyleColor());
    }
}
