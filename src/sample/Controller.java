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
import java.util.*;

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
                    for(int k = 0; k < 3; k++) {
                        int a = sc.nextInt();
                        if(a != -1) pipes[i][j].dirs.add(a);
                    }
                    Collections.sort(pipes[i][j].dirs);
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
                    // call our logics, check if they are connected afterwards
                    // rotot.rotate(i,j)
                    // if(connected) gimme path
                    // go to new state
                    int[] ids = (int[]) iv.getUserData();
                    //if(ids[0] == -1 || ids[1] == -1) return;
                    Pipe target = pipes[ids[0]][ids[1]];
                    if(target.dirs.isEmpty()) return;
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
                        level.add(iw, x.second+1, x.first+1);
                    }
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
