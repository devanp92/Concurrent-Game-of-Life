package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import server.ClientConnection;
import server.Server;
import server.UICallback;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;


/**
 * Created by Daniel on 4/5/2014.
 */
public class mainPageController implements UICallback {
    public Button connectButton;
    ClientConnection connection = null;
    public Label title;
    public Label boardDimensionsLabel;
    //private Map<String,Rectangle> gridMap;
    public TextField size;
    public GridPane displayGrid;
    public Label initializedBoardDimensionsLabel;
    public Button buildGridButton;
    public Button pauseGameButton;
    public Label statusLabel;
    public Button resumeGameButton;
    public TextField serverIpAddress;
    private int gridSize = 0;
    private boolean gridClickedOn = false;

    public void startNewGame(ActionEvent event) {
        if(!size.getText().isEmpty()) {
            gridSize = Integer.parseInt(size.getText());
            size.clear();
            initializeBoard(gridSize);
            inGameStatus();
            setStatusLabel("Click on board to start Game!");
        }
        else
        {
            setStatusLabel("Please input a Grid size!!");
        }
    }

    public void quit() {
        System.exit(0);
    }

    private void initializeBoard(int size)
    {
       statusLabel.setVisible(false);
       for(Integer i = 0; i < size; i++)
       {
            for (Integer j = 0; j <size; j++)
            {
                final Rectangle recta = new Rectangle(20,20);
                recta.setOnMouseClicked(new EventHandler<Event>(){
                    @Override
                    public void handle(Event event) {
                        if(!gridClickedOn)
                        {
                            gridClickedOn = true;
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
        buildGridButton.setVisible(false);
        initializedBoardDimensionsLabel.setVisible(true);
        pauseGameButton.setVisible(true);
        initializedBoardDimensionsLabel.setText(initializedBoardDimensionsLabel.getText() + gridSize +" X " + gridSize);
    }
    private void setStatusLabel(String newSatus){
        statusLabel.setVisible(true);
        statusLabel.setText(newSatus);
    }

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
        System.out.println("resume functionality needs to be implemented");
    }

    public void startConnection(ActionEvent actionEvent)
    {
        try {
            connection = new ClientConnection(new Socket(serverIpAddress.getText(), Server.port));
            connection.subscribe(this);
            setStatusLabel("Server Connection Started");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateGame() {
        for(int i = 0; i < gridSize; i++)
        {
            for(int j = 0; j < gridSize; j++)
            {
                if(connection.getGrid().convertGridTo2DArray()[i][j].getCellState() == 0)
                {
                    Rectangle rectangle = (Rectangle) displayGrid.getChildren().get((i*gridSize)+ j);
                    rectangle.setFill(Color.WHITE);
                }
                else
                {
                    Rectangle rectangle = (Rectangle) displayGrid.getChildren().get((i*gridSize)+ j);
                    rectangle.setFill(Color.BLACK);
                }
            }
        }
    }
}
