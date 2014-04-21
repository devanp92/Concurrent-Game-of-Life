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
	public static int numOfClientThreads = -1;

    public static void main(String[] args) {
    	if(args.length > 0) {
    		try {
    			int numOfThreads = Integer.parseInt(args[0]);
    			if(numOfThreads <= 0) throw new NumberFormatException();
    			else {
    				numOfClientThreads = numOfThreads;
    			}
    		}
    		catch(NumberFormatException e) {
    			System.err.println("Input a positive integer or no argument for default");
    		}
    	}

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
