import com.jfoenix.controls.JFXSpinner;
import javafx.event.EventHandler;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import me.artish1.auction.AuctionItem;
import me.artish1.threads.DownloadImageListCell;
import me.artish1.web.Browsing;


public class AuctionListCell<T> extends ListCell<AuctionItem> {
    private EventHandler<MouseEvent> clickHandler;

    AuctionListCell() {

    }

    @Override
    protected void updateItem(AuctionItem item, boolean empty) {
        super.updateItem(item, empty);
        if (item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        clickHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Browsing.openWebpage(item.getItemLink());
            }
        };


        setText(item.getName());

        JFXSpinner loadingSpinner = new JFXSpinner();
        loadingSpinner.setRadius(-1);
        loadingSpinner.setProgress(-1);





        setOnMouseClicked(clickHandler);


        if(getTooltip() == null){
            Tooltip tooltip = new Tooltip();
            tooltip.setGraphic(loadingSpinner);
            Tooltip.install(this,tooltip);
            DownloadImageListCell thread = new DownloadImageListCell(tooltip,item);
            thread.start();
        }


    }

}
