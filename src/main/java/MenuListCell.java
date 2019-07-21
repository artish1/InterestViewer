
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import me.artish1.menu.MenuItem;


public class MenuListCell<T> extends ListCell<MenuItem> {
    @Override
    protected void updateItem(MenuItem item, boolean empty) {
        super.updateItem(item, empty);
        Node thisNode = this;
        if (item == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        item.setCell(thisNode);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                item.onMouseHover(thisNode);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                item.onMouseExit(thisNode);
            }
        });


        setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                item.onClick(thisNode);

            }
        });

        ImageView view = new ImageView(item.getIcon());
        view.setFitWidth(32);
        view.setFitHeight(32);

        setGraphicTextGap(13);
        setGraphic(view);
        setText(item.getTitle());


    }
}
