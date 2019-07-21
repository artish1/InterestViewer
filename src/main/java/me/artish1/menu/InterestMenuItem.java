package me.artish1.menu;

import javafx.scene.Node;

public class InterestMenuItem extends MenuItem {
    private Node screen;
    private Node[] screensOff;

    public InterestMenuItem(Node screenToChangeTo, Node... screensOff) {
        super("Interests", "/interest.png", screenToChangeTo, screensOff);
        this.screen = screenToChangeTo;
        this.screensOff = screensOff;
        setStyleColor("#FA8987");
    }


    //Cleanup?
    @Override
    public void onClick(Node n) {
        super.onClick(n);

        setStyleOnHover(getStyleColor());
        n.setStyle("-fx-background-color:" + getStyleColor());
    }

}
