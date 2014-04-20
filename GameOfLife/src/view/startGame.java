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
        Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        root.getStylesheets().add("view/css/Style.css");
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(new Scene(root, 550, 550));
        primaryStage.show();
    }
}
