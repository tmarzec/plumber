package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.Scanner;

public class Controller {

    int X_SIZE;
    int Y_SIZE;

    @FXML
    private Button load_button;

    @FXML
    private FlowPane flow_pane;

    @FXML
    private GridPane level;

    Pipe[][] pipes;

    Image get_img(Pipe of) {
        if(of.on == -1 || of.tw == -1) return new Image("blank.png");
        return new Image("s"+Math.min(of.on, of.tw) + ""+Math.max(of.on, of.tw) +".png");
    }

    void load_level(String name) {
        level.getChildren().clear();
        //level.getChildren().removeAll(level.getChildren());
        // x_sz, y_sz
        try {
            Scanner sc = new Scanner(new File(name));
            X_SIZE = sc.nextInt();
            Y_SIZE = sc.nextInt();
            pipes = new Pipe[X_SIZE][Y_SIZE];
            for(int i = 0; i < X_SIZE; i++) {
                for(int j = 0; j < Y_SIZE; j++) {
                    // pair is enough for now
                    pipes[i][j] = new Pipe();
                    pipes[i][j].on = sc.nextInt();
                    pipes[i][j].tw = sc.nextInt();
                }
            }
        } catch (Exception f) {
            f.printStackTrace();
            System.exit(-1);
        }
        //mainStage.setX(500);
        //mainStage.setY(500);
        mainStage.setWidth(100*Y_SIZE + 200);
        mainStage.setHeight(100*X_SIZE + 200);
        level.setPrefHeight((X_SIZE-1) * 110);
        level.setPrefWidth((Y_SIZE-1) * 110);
        level.setGridLinesVisible(true);
        //level.setHgap(3);
        //level.setVgap(3);
        level.setPadding(new Insets(10, 10, 10, 10));
        for(int i = 0; i < X_SIZE; i++) {
            for(int j = 0; j < Y_SIZE; j++) {
                ImageView iv = new ImageView(get_img(pipes[i][j]));
                //iv.setFitHeight(200);
                //iv.setFitWidth(100);
                iv.setUserData(new int[]{i, j});
                //iv.setFitWidth(100);
                //iv.setFitHeight(100);
                iv.setOnMouseClicked(mouseEvent -> {
                    // go to new state
                    int[] ids = (int[]) iv.getUserData();
                    //if(ids[0] == -1 || ids[1] == -1) return;
                    Pipe target = pipes[ids[0]][ids[1]];
                    if(target.on == -1 || target.tw == -1) return;
                    target.on = (target.on + 1) % 4;
                    target.tw = (target.tw + 1) % 4;
                    iv.setImage(get_img(target));
                });
                level.add(iv, j+1, i+1);
                //GridPane.setMargin(iv, new Insets(-1, -1, -1, -1));

            }
        }
        // set contents
    }


    @FXML
    void load_next(ActionEvent event) {
        load_level("first.lvl");
    }

    public Stage mainStage;

    public void magic() {
        //level.prefHeightProperty().bind(Bindings.size(itemListProperty).multiply(100));;
        //ColumnConstraints cc = new ColumnConstraints(100);
        //level.getColumnConstraints().addAll(cc, cc);
        //RowConstraints rc = new RowConstraints(100);
        //level.getRowConstraints().addAll(rc, rc);
        //load level?
        load_level("0.lvl");
        //mainStage.setX();
    }

}
