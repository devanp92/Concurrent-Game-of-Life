package view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Daniel on 4/5/2014.
 */
public class mainPageController implements Initializable{

    public Label title;
    public Label boardDimensionsLabel;
    public TextField size;
    public GridPane displayGrid;
    public Label initializedBoardDimensionsLabel;
    public Button startGameButton;
    public Button pauseGameButton;
    public Label statusLabel;
    public Button resumeGameButton;
    private int gridSize = 0;
    private boolean gridclickedOn = false;

    public void startNewGame(ActionEvent event) {
        if(!size.getText().isEmpty()) {
            gridSize = Integer.parseInt(size.getText());
            size.clear();
            initializeBoard(gridSize);
            inGameStatus();
            statusLabel.setText("Click on board to start Game!");
            statusLabel.setVisible(true);
        }
        else
        {
            statusLabel.setText("Please input a Grid size!!");
            statusLabel.setVisible(true);
        }
    }

    public void quit() {
        System.exit(0);
    }

    private void initializeBoard(int size)
    {
       statusLabel.setVisible(false);
       for(int i = 0; i < size; i++)
       {
            for (int j = 0; j <size; j++)
            {
                Rectangle recta = new Rectangle(20,20);
                recta.setOnMouseClicked(new EventHandler<Event>(){
                    @Override
                    public void handle(Event event) {
                        if(!gridclickedOn)
                        {
                            gridclickedOn = true;
                            statusLabel.setText("The Game has started!!!");
                            if (recta.getFill().equals(Color.BLACK))
                            {
                                recta.setFill(Color.WHITE);
                            }
                            else
                            {
                                recta.setFill(Color.BLACK);
                            }
                        }
                    }
                });
                recta.setFill(Color.WHITE);
                displayGrid.add(recta,i,j);
            }
        }
        displayGrid.setVisible(true);
        displayGrid.setMaxHeight(size*20);
        displayGrid.setMaxWidth(size*20);
    }
    private void inGameStatus()
    {
        boardDimensionsLabel.setVisible(false);
        size.setVisible(false);
        initializedBoardDimensionsLabel.setVisible(true);
        startGameButton.setVisible(false);
        pauseGameButton.setVisible(true);
        initializedBoardDimensionsLabel.setText(initializedBoardDimensionsLabel.getText() + gridSize +" X " + gridSize);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){}

    public void pauseGame(ActionEvent event) {
        //TO DO pause functionality needs to be implemented
        statusLabel.setText("The game is paused");
        pauseGameButton.setVisible(false);
        resumeGameButton.setVisible(true);
        System.out.println("pause functionality needs to be implemented");
    }

    public void resumeGame(ActionEvent event) {
        resumeGameButton.setVisible(false);
        pauseGameButton.setVisible(true);
        statusLabel.setText("The game has resumed");
    }
}
