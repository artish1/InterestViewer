package me.artish1.threads;

import javafx.application.Platform;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.artish1.auction.AuctionItem;

public class DownloadImageListCell extends Thread {

    private Tooltip t;
    private AuctionItem item;


    public DownloadImageListCell(Tooltip t, AuctionItem item) {
        this.item = item;
        this.t = t;
    }

    /**
     * Downloads the image and sets the graphic of
     * the tooltip provided in the constructor once finished.
     */
    @Override
    public void run() {
        Image img = new Image(item.getImageUrl());
        ImageView view = new ImageView(img);
        view.setImage(img);
        Platform.runLater(() -> t.setGraphic(view));
    }
}
