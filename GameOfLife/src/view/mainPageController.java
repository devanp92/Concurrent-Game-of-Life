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
    //Local variables
    ClientConnection connection = null;
    private boolean connectionStarted = false;
    private volatile int gridSize = 0;
    private boolean displayInitialized = false;
    //private boolean gridInitialized = false;
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
                setStatusLabel("Server Connection Started", "green");
                connectionStarted = true;
                serverIP = serverIpAddress.getText();
                serverIpAddress.setDisable(true);
                connectButton.setVisible(false);
                currentBoardDimensionsLabel.setVisible(true);
                boardDimensionsLabel.setVisible(true);
                size.setVisible(true);
                resizeGridButton.setVisible(true);
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
        event.consume();
    }
    /*
    private void beginTheGame(Rectangle rectangle)
    {
        String[] indices = rectangle.getId().split(",");
        connection.startLife(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]));

    }
    */
    public void quit() {
        System.exit(0);
        //TODO: these never get executed, remove or call Platform.runLater() before System.exit(0)
        System.out.println(displayGrid.getChildren().removeAll(displayGrid.getChildren()));
        System.out.println(displayGrid.getChildren().size());
    }

    /*private void initializeBoard(int size)//TODO: REMOVE
    {
        statusLabel.setVisible(false);
        for(Integer i = 0; i < size; i++)
        {
            for (Integer j = 0; j <size; j++)
            {
                final Rectangle recta = new Rectangle(20,20);
                recta.setId(i.toString() + "," + j.toString());
                recta.setOnMouseClicked(new EventHandler<Event>()
                {
                    @Override
                    public void handle(Event event)
                    {
                        if (!connection.getIsPlaying())
                        {
                            //setStatusLabel("The Game has started!", "green");
                            beginTheGame((Rectangle) event.getSource());
                            if (recta.getFill().equals(Color.BLACK))
                            {
                                recta.setFill(Color.WHITE);
                            }
                            else
                            {
                                recta.setFill(Color.BLACK);
                            }
                        }
                        String xy = recta.getId();
                        int x = Integer.valueOf(xy.split(",")[0]);
                        int y = Integer.valueOf(xy.split(",")[1]);
                        connection.changeCellState(x,y,(recta.getFill() == Color.BLACK) ? 1 : 0);
                    }
                });
                recta.setFill(Color.WHITE);
                displayGrid.add(recta,i,j);
            }
        }
        displayGrid.setVisible(true);
        displayGrid.setMaxHeight(size*20);
        displayGrid.setMaxWidth(size*20);
    }*/
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
        setStatusLabel("The game is on paused", "yellow");
        pauseGameButton.setVisible(false);
        playGameButton.setVisible(true);
        connection.pause();
        event.consume();
    }

    public void playGame(ActionEvent event)
    {
        playGameButton.setVisible(false);
        pauseGameButton.setVisible(true);
        setStatusLabel("The game is on play", "green");
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
            //displayGrid.getChildren().removeAll(displayGrid.getChildren());

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
                    final Rectangle recta = new Rectangle(20, 20);
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
                    recta.setFill((connection.getGrid().convertGridTo2DArray()[ycord][xcord].getCellState() == 1)? Color.BLACK : Color.WHITE );
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
                            displayGrid.setMaxHeight(gridSize * 20);
                            displayGrid.setMaxWidth(gridSize * 20);
                        }
                    });
                }
            }


            //after changing the dimension of the grid color the grid
            //colorDisplayGrid();
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
                        if(connection.getIsPlaying()) {
                            //rectangle.setFill((connection.getGrid().convertGridTo2DArray()[col][row].getCellState() == 1) ? Color.BLACK : Color.WHITE);
                        	rectangle.setFill((connection.getGrid().getCell(col, row).getCellState() == 1) ? Color.BLACK : Color.WHITE);
                        }
                    }
                });
            }
        }
    }

    //private void iterateAndDisplayGrid

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
    public void updatePausePlay(NetworkMessage nm) {//TODO
    	switch(nm) {
			case CLEAR:
				break;
			case PAUSE:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pauseGameButton.setVisible(false);
                        playGameButton.setVisible(true);
                    }
                });
                break;
			case PLAY:
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pauseGameButton.setVisible(true);
                        playGameButton.setVisible(false);
                    }
                });
				break;
			default:
				break;
    	}
    }
    
    @Override
    public void updateIterationDelay(int val) {
    	//TODO: set the iteration delay textfield to val
    }
}
