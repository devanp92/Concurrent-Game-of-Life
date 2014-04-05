package view;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by Daniel on 3/29/2014.
 */


public class mainPageController {


    public GridPane grid;
    public Rectangle recta11;
    public Rectangle recta12;
    public Rectangle recta21;
    public Rectangle recta22;
    public Label title;

    public void click()
    {
        ObservableList<Node> children = grid.getChildren();

        System.out.println(children.indexOf(recta11));

        if(grid.isGridLinesVisible())
        {
            grid.setGridLinesVisible(false);
        }
        else
        {
            grid.setGridLinesVisible(true);
            click1();
        }
    }
    public void click1()
    {
        recta11.setFill(Color.BLACK);
        recta11.setStroke(Color.BLACK);

        if(recta11.getHeight() == 200)
        {
            recta11.setHeight(150);
            recta11.setWidth(50);
        }
        else
        {
            recta11.setHeight(200);
            recta11.setWidth(200);
        }
    }


    public void newGame(ActionEvent actionEvent) {
    }

    public void quit(ActionEvent actionEvent) {
        System.exit(0);
    }




}
