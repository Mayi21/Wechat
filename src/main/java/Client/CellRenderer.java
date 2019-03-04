package Client;

import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * A Class for Rendering users images / name on the userlist.
 */
class CellRenderer implements Callback<ListView<User>,ListCell<User>>{
        @Override
    public ListCell<User> call(ListView<User> p) {
        ListCell<User> cell = new ListCell<User>(){

            @Override
            protected void updateItem(User user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();
                    Text name = new Text(user.getId());
                    hBox.getChildren().addAll(name);
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}