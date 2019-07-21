package me.artish1.menu;

import javafx.css.PseudoClass;
import javafx.scene.Node;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;


public class MenuItem {

    public static List<MenuItem> menus = new ArrayList<>();

    private Image icon;
    private String title;
    private Node screen;
    private Node cell;
    private Node[] screensOff;
    private String styleColor;
    private static String styleOnHover;
    private static final String prevStyle = "-fx-background-color: #3D4956";


    public MenuItem(String title, String imagePath, Node screenOn, Node... screensOff) {
        this.title = title;
        this.icon = new Image(imagePath); //example of a path to resource folder: "/drawIcon.png"
        this.screen = screenOn;
        this.screensOff = screensOff;
    }

    public static void setStyleOnHover(String style) {
        styleOnHover = style;
    }

    public Image getIcon() {
        return icon;
    }

    public String getTitle() {
        return title;
    }

    public void setStyleColor(String styleColor) {
        this.styleColor = styleColor;
    }

    public String getStyleColor() {
        return styleColor;
    }

    public void onClick(Node ns) {
        for (Node n : screensOff) {
            n.setVisible(false);
        }
        screen.setVisible(true);

        ns.pseudoClassStateChanged(PseudoClass.getPseudoClass("selected"), true);

        updateOthers(this);

    }

    public void setCell(Node cell) {
        this.cell = cell;
    }

    public void onMouseHover(Node n) {
        n.setStyle("-fx-background-color: " + styleOnHover);
    }

    public void onMouseExit(Node n) {
        boolean selected = false;
        for (PseudoClass c : n.getPseudoClassStates()) {
            if (c.getPseudoClassName().equalsIgnoreCase("selected")) {
                selected = true;
                break;
            }
        }
        if (!selected)
            n.setStyle(prevStyle);
    }


    public void update() {
        boolean selected = false;
        boolean hover = false;

        if (cell == null) {
            System.out.println("Cell is null for " + title);
            return;
        }

        for (PseudoClass c : cell.getPseudoClassStates()) {
            if (c.getPseudoClassName().equalsIgnoreCase("selected")) {
                selected = true;
                continue;
            } else {
                if (c.getPseudoClassName().equalsIgnoreCase("hover"))
                    hover = true;
                continue;
            }
        }

        if (selected)
            cell.setStyle("-fx-background-color: " + getStyleColor());
        else {
            if (hover) {
                cell.setStyle("-fx-background-color: " + styleOnHover);
            } else
                cell.setStyle(prevStyle);
        }

    }

    public static void updateOthers(MenuItem current) {
        for (MenuItem item : menus) {
            if (!item.equals(current))
                item.update();
        }
    }


    @Override
    public String toString() {
        return getTitle();
    }


}
