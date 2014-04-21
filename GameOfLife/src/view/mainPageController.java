package view;

import backend.Cell;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import server.ClientConnection;
import server.NetworkMessage;
import server.Server;
import server.UICallback;

import java.io.IOException;
import java.net.Socket;


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
    public Label currentBoardDimensionsLabel;
    public Button resizeGridButton;
    public Button pauseGameButton;
    public Label statusLabel;
    public Button playGameButton;
    public TextField serverIpAddress;
    public Button delayButton;
    public TextField delayInput;
    public Label delayLabel;
    public Button serverButton;
    public Button closeServerButton;
    public Button renderUI;
    public GridPane menu;
    //Local variables
    ClientConnection connection = null;
    Server serverThread = null;
    private boolean connectionStarted = false;
    private volatile int gridSize = 0;
    private boolean displayInitialized = false;
    private String serverIP = "";

    public void startConnection(ActionEvent actionEvent)
    {
        try {
            connection = new ClientConnection(new Socket(serverIpAddress.getText(), Server.port));
            connection.setDaemon(true);
            connection.subscribe(this);
            connection.start();
            if(connection != null)
            {
                setStatusLabel("Connected to server", "green");
                //TODO This was behind the board
                connectionStarted = true;
                serverIP = serverIpAddress.getText();
                connectButton.setVisible(false);
                serverIpAddress.setDisable(true);
                currentBoardDimensionsLabel.setVisible(true);
                boardDimensionsLabel.setVisible(true);
                size.setVisible(true);
                resizeGridButton.setVisible(true);
                delayLabel.setVisible(true);
                delayInput.setVisible(true);
                delayButton.setVisible(true);
                closeServerButton.setVisible(true);
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
    public void resizeGridAction(ActionEvent event) {
        if(connectionStarted)
        {
        	if(!connection.getIsPlaying()) { 
	            try
	            {
	                if (!size.getText().isEmpty())
	                {
	                    connection.resizeGrid(Integer.parseInt(size.getText()));
	                    size.clear();
	                    updateGame();
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
        	else {
        		setStatusLabel("Can't resize while playing","red");
                size.clear();
        	}
        }
        event.consume();
    }

    public void quit() {
        System.exit(0);
        //TODO: these never get executed, remove or call Platform.runLater() before System.exit(0)
    }

    private void inGameStatus()
    {
        boardDimensionsLabel.setVisible(true);
        size.setVisible(true);
        resizeGridButton.setVisible(true);
        //TODO initializedBoardDimensionsLabel.setVisible(true);
        //TODO initializedBoardDimensionsLabel.setText(initializedBoardDimensionsLabel.getText() + gridSize +" X " + gridSize);
    }
    public void setStatus(final String newStatus, final String txtFill) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
                setStatusLabel(newStatus, txtFill);
            }
        });
    }
    private void setStatusLabel(String newStatus, String txtFill)
    {
        statusLabel.setText(newStatus);
        //setting the color of the status label
        statusLabel.setStyle("-fx-font-size: 18pt;-fx-text-fill: " + txtFill);
        statusLabel.setVisible(true);
    }

    public void pauseGame(ActionEvent event)
    {
    	//DON'T set anything: the visual effects will change when the official message comes from the server
        //setStatusLabel("The game is paused", "yellow");
        //pauseGameButton.setVisible(false);
        //playGameButton.setVisible(true);
        connection.pause();
        event.consume();
    }

    public void playGame(ActionEvent event)
    {
    	//DON'T set anything: the game might already be playing when the PLAY is received and the UI won't know
    	//The visual effects will change when the official message comes from the server
        //playGameButton.setVisible(false);
        //pauseGameButton.setVisible(true);
        //setStatusLabel("The game is playing", "green");
        connection.play();
        event.consume();
    }

    @Override
    /*update game is called when the server sends a new "grid" to the user, init connect, resize, during play(iteration action process)*/
    public void updateGame()
    {
        System.out.println("updateGame called, GridSize: " + connection.getGrid().getNumRows());
        //if the size of the client is not the same as the size in the server the board will readjust
        if((connection.getGrid().getNumRows() != gridSize)/* || !displayInitialized*/)
        {
            displayInitialized = true;
            gridSize = connection.getGrid().getNumRows();

            Platform.runLater(new Runnable()
            {
                @Override
                public void run()
                {
                    displayGrid.getChildren().clear();
                    //Displays the size of the grid to the user
                    currentBoardDimensionsLabel.setText("The grid size is: " + gridSize + " X " + gridSize);
                    playGameButton.setVisible(!connection.getIsPlaying());
                    pauseGameButton.setVisible(connection.getIsPlaying());
                }
            });



            for(Integer i = 0; i < gridSize; i++)
            {
                for (Integer j = 0; j < gridSize; j++)
                {
                    final Rectangle recta = new Rectangle(10, 10);
                    final int xcord = j;
                    final int ycord = i;
                    recta.setId(i.toString() + "," + j.toString());
                    recta.setOnMouseClicked(new EventHandler<Event>()
                    {
                        @Override
                        public void handle(Event event)
                        {
                            if (!connection.getIsPlaying())
                            {
                                //setStatusLabel("The Game has started!", "green");
                                if (recta.getFill().equals(Color.BLACK))
                                {
                                    recta.setFill(Color.WHITE);
                                    connection.changeCellState(xcord, ycord, 0);
                                }
                                else
                                {
                                    recta.setFill(Color.BLACK);
                                    connection.changeCellState(xcord, ycord, 1);
                                }
                            }
                        }
                    });
                    recta.setFill((connection.getGrid().getCell(xcord, ycord).getCellState() == 1) ? Color.BLACK : Color.WHITE);
                        /*
                            This call and everything inside it will update the FX thread still need some tweaking to do.
                         */
                    Platform.runLater(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            displayGrid.add(recta, xcord, ycord);
                            displayGrid.setVisible(true);
                            displayGrid.setMaxHeight(gridSize * 10);
                            displayGrid.setMaxWidth(gridSize * 10);
                        }
                    });
                }
            }
        }
        else
        {
            colorDisplayGrid();
        }
    }
    private void colorDisplayGrid()
    {
        //Fill color the board respectively to the states of the cells
        System.out.println(displayGrid.getChildren().size());
        for (Integer i = 0; i < gridSize; i++)
        {
            for (Integer j = 0; j < gridSize; j++)
            {
                final int row = i;
                final int col = j;
                Platform.runLater(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Rectangle rectangle = (Rectangle) displayGrid.getChildren().get((row * gridSize) + col);
                        rectangle.setFill((connection.getGrid().getCell(col, row).getCellState() == 1) ? Color.BLACK : Color.WHITE);
                    }
                });
            }
        }
    }

    @Override
    /*update cell is called when another client changes the state of a cell and updates the board in each of the other clients*/
    public void updateCell(Cell c) {
        final int row = c.y;
        final int col = c.x;
        final int cellState = c.getCellState();
        Platform.runLater(new Runnable()
        {
            @Override
            public void run()
            {
                Rectangle rectangle = (Rectangle) displayGrid.getChildren().get((row * gridSize) + col);
                rectangle.setFill((cellState == 1) ? Color.BLACK:Color.WHITE);
            }
        });
    }

    @Override
    public void updatePausePlay(NetworkMessage nm) {
    	switch(nm) {
			case CALCULATION_COMPLETE:
			case PAUSE:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pauseGameButton.setVisible(false);
                        playGameButton.setVisible(true);
                        setStatusLabel("The game is paused", "yellow");
                    }
                });
                break;
			case PLAY:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pauseGameButton.setVisible(true);
                        playGameButton.setVisible(false);
                        setStatusLabel("The game is playing", "green");
                    }
                });
				break;
			default:
				break;
    	}
    }
    
    @Override
    public void updateIterationDelay(final int val) {
    	Platform.runLater(new Runnable() {
            @Override
            public void run() {
                delayInput.setPromptText("current: " + String.valueOf(val));
                setStatusLabel("Delay Updated", "green");
            }
        });
    }

    public void setDelay(ActionEvent event) {
    	try
        {
    		int delayVal = Integer.parseInt(delayInput.getText());
    		if(delayVal > -1)
            {
                connection.updateDelayValue(delayVal);
                delayInput.clear();
                delayInput.setPromptText("current: " + delayVal);
                setStatusLabel("Delay Updated", "green");
            }
            else
            {
                setStatusLabel("Non-negative delay only", "red");
                delayInput.clear();
                delayInput.setPromptText("+Integers");
            }
    	}
    	catch(IllegalArgumentException e)
        {
    		setStatusLabel("Integers only", "red");
            delayInput.clear();
            delayInput.setPromptText("+Integers");
    	}
    	event.consume();
    }

    public void spawnServer(ActionEvent event) {
    	try {
			//Server s = new Server(10);
		    serverThread = new Server(Server.defaultSize);
			serverThread.start();
            //Update UI
            serverButton.setDisable(true);
            serverButton.setText("Server Started");
            serverButton.setStyle("-fx-text-fill: green;");
		}
		catch(Exception e) {
			setStatusLabel("Unable to start Server", "red");
		}
    	event.consume();
    }

    public void closeServer(ActionEvent event) {
    	if(serverThread != null) {
    		serverThread.stopServer();
    	}
        event.consume();
    }

    public void renderUI(ActionEvent actionEvent) {
        renderUI.setVisible(false);
        menu.setVisible(true);
        System.out.println(delayLabel.getLayoutX());
    }
}
