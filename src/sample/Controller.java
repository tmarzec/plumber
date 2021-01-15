package sample;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.*;

public class Controller {

    int X_SIZE;
    int Y_SIZE;


    @FXML
    private Button load_button;

    @FXML
    private FlowPane flow_pane;

    private Button level_button;

    @FXML
    private GridPane level;

    Pipe[][] pipes;

    Text count_holder = new Text();
    Text level_holder = new Text();
    int counter = 0;

    Image get_img(Pipe of) {
        if(of.dirs.isEmpty()) return new Image("blank.png");
        StringBuilder sb = new StringBuilder();
        sb.append('s');
        for(int x : of.dirs) {
            sb.append(x);
        }
        sb.append(".png");
        return new Image(new String(sb));
    }
    Image get_watery(Pipe of) {
        StringBuilder sb = new StringBuilder();
        sb.append('w');
        for(int x : of.dirs) {
            sb.append(x);
        }
        sb.append(".png");
        return new Image(new String(sb));
    }

    void set_level_act(ActionEvent ae) {
        System.out.println("ala");
    }

    void load_level(String name) {
        level.getChildren().clear();
        counter = 0;
        level_holder.setText("Level "+name);
        level_holder.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 19));
        level.add(level_holder, 6, 1);
        name = "levels/" + name + ".lvl";
        try {
            Scanner sc = new Scanner(new File(name));
            X_SIZE = sc.nextInt();
            Y_SIZE = sc.nextInt();
            int am = sc.nextInt();
            pipes = new Pipe[X_SIZE][Y_SIZE];
            for(int i = 0; i < X_SIZE; i++) {
                for(int j = 0; j < Y_SIZE; j++) {
                    // pair is enough for now
                    pipes[i][j] = new Pipe();
                    for(int k = 0; k < am; k++) {
                        int a = sc.nextInt();
                        if(a == -1 && am == 2) {
                            break;
                        }
                        if(a != -1) pipes[i][j].dirs.add(a);
                    }
                    Collections.sort(pipes[i][j].dirs);
                }
            }
        } catch (Exception f) {
            f.printStackTrace();
            System.exit(-1);
        }
        mainStage.setWidth(100*Y_SIZE + 200);
        mainStage.setHeight(100*X_SIZE + 200);
        level.setPrefHeight((X_SIZE) * 110);
        level.setPrefWidth((Y_SIZE) * 110);
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
                    // call our logics, check if they are connected afterwards
                    // rotot.rotate(i,j)
                    // if(connected) gimme path
                    // go to new state
                    int[] ids = (int[]) iv.getUserData();
                    Pipe target = pipes[ids[0]][ids[1]];
                    if(target.dirs.isEmpty()) return;
                    counter += 1;
                    count_holder.setText("Moves:\n"+counter);
                    for(int k = 0; k < target.dirs.size(); k++) {
                        target.dirs.set(k, (target.dirs.get(k)+1)%4);
                    }
                    Collections.sort(target.dirs);
                    iv.setImage(get_img(target));
                    Optional<ArrayList<Solver.Pair>> o = Solver.connected(pipes, X_SIZE, Y_SIZE);
                    if(o.isEmpty()) return;
                    // color 'em blue
                    for(Solver.Pair x : o.get()) {
                        // update pair x
                        ImageView iw = new ImageView(get_watery(pipes[x.first][x.second]));
                        level.add(iw, x.second+2, x.first+2);
                    }
                    level.add(new ImageView(new Image("bonsai_happy.jpg")), Y_SIZE+2, X_SIZE+1);
                    level.add(new ImageView(new Image("wkran.png")), 2, 1);
                    level.add(new ImageView(new Image("wkoniec.png")), Y_SIZE+2, X_SIZE);

                });
                level.add(iv, j+2, i+2);
                //GridPane.setMargin(iv, new Insets(-1, -1, -1, -1));
            }
        }
        // set contents
        level.add(new ImageView(new Image("kran.png")), 2, 1);
        level.add(new ImageView(new Image("koniec.png")), Y_SIZE+2, X_SIZE);
        level.add(new ImageView(new Image("bonsai_sad.jpg")), Y_SIZE+2, X_SIZE+1);

        level.add(count_holder, Y_SIZE, 1);
        count_holder.setText("Moves:\n0");
        count_holder.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

        level_button = new Button();
        level_button.setText("Choose\nlevel");
        level.add(level_button, Y_SIZE+2, 1);
        level_button.setPrefWidth(100);
        level_button.setPrefHeight(100);
        level_button.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 15));
        level_button.setAlignment(Pos.CENTER);
        level_button.setTextAlignment(TextAlignment.CENTER);
        level_button.setOnAction(this::set_level_act);
    }


    @FXML
    void load_next(ActionEvent event) {
        load_level("10");
    }

    public Stage mainStage;

    public void magic() {

        load_level("9");
    }

}
