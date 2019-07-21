import com.jfoenix.controls.JFXSpinner;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import me.artish1.auction.AuctionItem;
import me.artish1.threads.DownloadImageListCell;
import me.artish1.web.Browsing;


public class AuctionListCell<T> extends ListCell<AuctionItem> {

    @Override
    protected void updateItem(AuctionItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Browsing.openWebpage(item.getItemLink());
            }
        });

        //set text of the cell
        setText(item.getName());

        //Tooltip
        //Create an indefinite loading spinner that will replace the image of the item as it loads.
        JFXSpinner loadingSpinner = new JFXSpinner();
        loadingSpinner.setRadius(-1);
        loadingSpinner.setProgress(-1);

        Tooltip tooltip = new Tooltip();
        tooltip.setGraphic(loadingSpinner);
        tooltip.setHideOnEscape(true);

        //Download image of item and set it to the tooltip once finished
        DownloadImageListCell thread = new DownloadImageListCell(tooltip, item);
        thread.start();

        Tooltip.install(this, tooltip);
        setTooltip(tooltip);


    }

}
