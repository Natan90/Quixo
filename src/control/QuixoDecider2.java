package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.io.FilterOutputStream;
import java.util.*;
import java.util.List;

public class QuixoDecider2 extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider2(Model model, Controller control) {
        super(model, control);
    }

    int max;
    QuixoBoard board;
    QuixoBoard simulatedBoard;


    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();
        board = stage.getBoard(); // get the board
        simulatedBoard = stage.getBoard();
        ContainerElement pot = null; // the pot where to take a pawn
        pot = stage.getRedPot();



        List<Point> valid = simulatedBoard.computeValidCells(true, null, model);
        // 0 4 -> A5

        int[] firstMove = new int[2];// [x, y]
        List<Point> allMoves = new ArrayList<>(); // [[x, y], [x, y]]
        int[] secondMoveTab = new int[2];


        for(int i=0; i<valid.size(); i++){
            int cubeRow = (int) valid.get(i).getX();// 0
            int cubeCol = (int) valid.get(i).getY(); // 4
            firstMove[0] = cubeRow;
            firstMove[1] = cubeCol;


            List<Point> secondMove = simulatedBoard.computeValidCells(false, firstMove , model);
            for(int j =0; j<secondMove.size(); j++){

                Point firstMoveCopy = new Point(firstMove[0], firstMove[1]);
                allMoves.add(firstMoveCopy);

                secondMoveTab[0] = (int) secondMove.get(j).getX();
                secondMoveTab[1] = (int) secondMove.get(j).getY();

                Point secondMoveCopy = new Point(secondMoveTab[0], secondMoveTab[1]);
                allMoves.add(secondMoveCopy);
            }
           }

        for (int i =0; i < allMoves.size(); i=i+2){


            // first coup
            int row = (int) allMoves.get(i).getY();
            int col = (int) allMoves.get(i).getX();
            Cube cube = (Cube) simulatedBoard.getElement(row, col);

            ActionList defaultAction = ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0);

            //second coup
            row = (int) allMoves.get(i+1).getY();
            col = (int) allMoves.get(i+1).getX();
            cube = (Cube) pot.getElement(row, col);

            cube = (Cube) pot.getElement(0, 0);

            ActionList deuxiemeCoup = ActionFactory.generatePutInContainer(model, cube, "quixoboard", row, col);



        }



        //faire une liste de tout les coups possibles


        QuixoController quixoController = (QuixoController) control;

        return null;


    }
}