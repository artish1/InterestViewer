package me.artish1.menu;

import javafx.scene.Node;

public class AboutMenuItem extends MenuItem {
    private Node screen;
    private Node[] screensOff;

    public AboutMenuItem(Node screenToChangeTo, Node... screensOff) {
        super("About", "/about.png", screenToChangeTo, screensOff);
        this.screen = screenToChangeTo;
        this.screensOff = screensOff;
        setStyleColor("#2ECC71");
    }

    @Override
    public void onClick(Node n) {
        super.onClick(n);
        n.setStyle("-fx-background-color: " + getStyleColor());
        setStyleOnHover(getStyleColor());

    }
}
