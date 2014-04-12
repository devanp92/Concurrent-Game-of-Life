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
    //FXML NODES
    public Button connectButton;
    public Label serverIpAddressPrompt;
    public Label title;
    public Label boardDimensionsLabel;
    public TextField size;
    public GridPane displayGrid;
    public Label initializedBoardDimensionsLabel;
    public Button buildGridButton;
    public Button pauseGameButton;
    public Label statusLabel;
    public Button resumeGameButton;
    public TextField serverIpAddress;
    ClientConnection connection = null;
    private boolean connectionStarted = false;
    private int gridSize = 0;
    private boolean gridClickedOn = false;
    private String serverIP = "";

    public void startConnection(ActionEvent actionEvent)
    {
        try {
            connection = new ClientConnection(new Socket(serverIpAddress.getText(), Server.port));
            connection.subscribe(this);
            connection.start();
            if(connection != null)
            {
                setStatusLabel("Server Connection Started", "green");
                connectionStarted = true;
                serverIP = serverIpAddress.getText();
                serverIpAddress.setDisable(true);
                connectButton.setVisible(false);
                boardDimensionsLabel.setVisible(true);
                size.setVisible(true);
                buildGridButton.setVisible(true);
            }
            else
            {
                setStatusLabel("Server Connection did not start","red");
            }
        }
        catch (java.net.UnknownHostException e)
        {
            setStatusLabel("Not a valid IP Address", "red");
            serverIpAddress.clear();
            serverIpAddress.setText("127.0.0.1");
        }
        catch (java.net.ConnectException e)
        {
            setStatusLabel("Server has not been initialized!","red");

        }
        catch (IOException e) {
            setStatusLabel("Connection to server Failed","red");
            e.printStackTrace();
        }
    }
    /*
        Sets up the grid after user defines a size for the
     */
    public void setUpNewGame(ActionEvent event) {
        if(connectionStarted)
        {
            try
            {
                if (!size.getText().isEmpty())
                {
                    gridSize = Integer.parseInt(size.getText());
                    size.clear();
                    connection.initializeGrid(gridSize);
                    initializeBoard(gridSize);
                    inGameStatus();
                    setStatusLabel("Click on board to start Game!", "green");
                } else
                {
                    setStatusLabel("Please input a Grid size!!", "red");
                }
            }
            catch (Exception e)
            {
                setStatusLabel("Not valid a valid grid size","red");
                size.clear();
            }
        }
    }
    private void beginTheGame(Rectangle rectangle)
    {
        String[] indices = rectangle.getId().split(",");
        connection.startLife(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]));

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
                recta.setId(i.toString() + "," + j.toString());
                recta.setOnMouseClicked(new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        if (!gridClickedOn) {
                            gridClickedOn = true;
                            setStatusLabel("The Game has started!", "green");
                            beginTheGame((Rectangle) event.getSource());
                            if (recta.getFill().equals(Color.BLACK)) {
                                recta.setFill(Color.WHITE);
                            } else {
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
    private void setStatusLabel(String newSatus, String txtFill)
    {
        statusLabel.setText(newSatus);
        //setting the color of the status label
        statusLabel.setStyle("-fx-font-size: 18pt;-fx-text-fill: " + txtFill);
        statusLabel.setVisible(true);
    }

    public void pauseGame(ActionEvent event)
    {
        setStatusLabel("The game is paused", "yellow");
        pauseGameButton.setVisible(false);
        resumeGameButton.setVisible(true);
        connection.pause();
        System.out.println("pause functionality needs to be implemented");
    }

    public void resumeGame(ActionEvent event)
    {
        resumeGameButton.setVisible(false);
        pauseGameButton.setVisible(true);
        setStatusLabel("The game has resumed", "green");
        connection.play();
        System.out.println("resume functionality needs to be implemented");
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
