package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = fxmlLoader.load();

        //root.mainStage = primaryStage;
        ((Controller)fxmlLoader.getController()).mainStage = primaryStage;
        //((Controller)fxmlLoader.getController()).magic();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 400, 600));

        ((Controller)fxmlLoader.getController()).magic();

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
