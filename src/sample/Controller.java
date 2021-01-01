package sample;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class Controller {

    int X_SIZE;
    int Y_SIZE;

    @FXML
    private GridPane level;

    Pipe[][] pipes;

    Image get_img(Pipe of) {
        return new Image(Math.min(of.on, of.tw) + ""+Math.max(of.on, of.tw) +".png");
    }

    void load_level(String name) {
        level.getChildren().clear();
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
        level.setPrefHeight(X_SIZE * 1000);
        level.setPrefWidth(Y_SIZE * 1000);
        level.setGridLinesVisible(true);

        for(int i = 0; i < X_SIZE; i++) {
            for(int j = 0; j < Y_SIZE; j++) {
                if(pipes[i][j].on == -1 || pipes[i][j].tw == -1) continue;
                ImageView iv = new ImageView(get_img(pipes[i][j]));
                //iv.setFitHeight(200);
                //iv.setFitWidth(100);
                iv.setUserData(new int[]{i, j});
                iv.setOnMouseClicked(mouseEvent -> {
                    // go to new state
                    int[] ids = (int[]) iv.getUserData();
                    Pipe target = pipes[ids[0]][ids[1]];
                    target.on = (target.on + 1) % 4;
                    target.tw = (target.tw + 1) % 4;
                    iv.setImage(get_img(target));
                });
                level.add(iv, i, j);
                //GridPane.setMargin(iv, new Insets(-1, -1, -1, -1));

            }
        }
        // set contents
    }

    @FXML
    public void initialize() {
        ColumnConstraints cs = new ColumnConstraints(100);
        //load level?
        load_level("0.lvl");
    }

}
