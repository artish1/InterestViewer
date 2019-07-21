package me.artish1.menu;

import javafx.scene.Node;

public class SettingsMenu extends MenuItem {
    private Node screen;
    private Node[] screensOff;

    public SettingsMenu(Node screenToChangeTo, Node... screensOff) {
        super("Settings", "/settings.png", screenToChangeTo, screensOff);
        this.screen = screenToChangeTo;
        this.screensOff = screensOff;
        setStyleColor("#03DAC6");
    }

    @Override
    public void onClick(Node n) {
        super.onClick(n);
        setStyleOnHover(getStyleColor());
        n.setStyle("-fx-background-color:" + getStyleColor());
    }
}
