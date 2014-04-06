package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Daniel on 3/29/2014.
 */
public class startGame extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("borderPane.fxml"));
        //root.getStylesheets().add("sample/myStyle.css");
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(new Scene(root, 500, 500));
        primaryStage.show();
    }
}
