package view;

import com.sun.org.apache.xerces.internal.util.SymbolTable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Daniel on 4/5/2014.
 */
public class borderPageController {

    public Label title;
    public Label boardDimensionsLabel;
    public TextField size;
    public GridPane displayGrid;

    public void newGame()
    {   //not getting error when I call method with here
        //System.out.println(size.getText().toString());
        int inputSize = Integer.getInteger(size.getText());
        size.clear();
        initializeBoard(inputSize);
    }
    public void quit()
    {
        System.exit(0);
    }
    public void initializeBoard(int size)
    {
        System.out.println(size);
        /* todo
        but i get error here
        for(int i = 0; i < size; i++){
            Rectangle recta = new Rectangle(20,20);
            recta.setFill(Color.BLACK);
            displayGrid.add(recta,1,1);
        }*/
    }
}
