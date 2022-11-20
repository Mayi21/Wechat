package client;

import entity.UserViewVo;
import javafx.geometry.Pos;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

/**
 * 这个貌似是用户列表
 * */
class CellRenderer implements Callback<ListView<String>,ListCell<String>>{
    @Override
    public ListCell<String> call(ListView<String> p) {
        ListCell<String> cell = new ListCell<String>(){
            @Override
            protected void updateItem(String user, boolean bln) {
                super.updateItem(user, bln);
                setGraphic(null);
                setText(null);
                if (user != null) {
                    HBox hBox = new HBox();
                    Text name = new Text(user);
                    hBox.getChildren().addAll(name);
                    hBox.setAlignment(Pos.CENTER);
                    setGraphic(hBox);
                }
            }
        };
        return cell;
    }
}