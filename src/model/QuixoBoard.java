package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;

import java.util.ArrayList;
import java.util.List;
import java.awt.*;

/**
 * quixo main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class QuixoBoard extends ContainerElement {
    public QuixoBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 3x3 grid, named "quixoboard", and in x,y in space
        super("quixoboard", x, y, 5 , 5, gameStageModel);
    }

    public void setValidCells(int number) {
        Logger.debug("called",this);
        resetReachableCells(false);
        List<Point> valid = computeValidCells(number);
        if (valid != null) {
            for(Point p : valid) {
                reachableCells[p.y][p.x] = true;
            }
        }
    }

    public List<Point> computeValidCells(int number) {
        List<Point> lst = new ArrayList<>();
        Pawn p = null;
        return lst;
    }
}
