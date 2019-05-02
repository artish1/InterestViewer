
import javafx.util.Callback;
import me.artish1.auction.AuctionItem;

public class AuctionCellFactory implements Callback{

    @Override
    public Object call(Object param) {
        return new AuctionListCell<AuctionItem>();
    }
}
