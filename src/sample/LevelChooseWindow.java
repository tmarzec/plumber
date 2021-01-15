package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.File;

public class LevelChooseWindow {

    public static class Level {
        int id;

        public Level(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Level "+ id;
        }
    }
    public Level lvl = null;
    @FXML
    private ListView<Level> listView;

    @FXML
    void apply_action(ActionEvent event) {
        if(!listView.getSelectionModel().isEmpty()) {
            lvl = listView.getSelectionModel().getSelectedItem();
            ((Stage)listView.getScene().getWindow()).close();
        }
    }

    @FXML
    public void initialize() {
        // load all files
        for(int i = 1; ; i++) {
            if(new File("levels/"+i+".lvl").exists()) {
                listView.getItems().add(new Level(i));
            }
            else {
                break;
            }
        }

    }
}
