package view;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Daniel on 3/29/2014.
 */
/*This class should hold the client connection Object try to see how to make it happen when the user presses the connect button */
public class startGame extends Application{

    public static void main(String[] args) {

        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainPage.fxml"));
        //root.getStylesheets().add("sample/myStyle.css");
        primaryStage.setTitle("Game of Life");
        primaryStage.setScene(new Scene(root, 800, 800));
        primaryStage.show();
    }
}
