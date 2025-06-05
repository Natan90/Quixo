package control;

import boardifier.control.*;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.PutInContainerAction;
import boardifier.model.action.RemoveFromContainerAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.ElementLook;
import boardifier.view.GridLook;
import boardifier.view.View;
import javafx.event.*;
import javafx.geometry.Point2D;
import javafx.scene.input.*;
import model.Cube;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * A basic mouse controller that just grabs the mouse clicks and prints out some informations.
 * It gets the elements of the scene that are at the clicked position and prints them.
 */
public class QuixoMouseController extends ControllerMouse implements EventHandler<MouseEvent> {

    public QuixoMouseController(Model model, View view, Controller control) {
        super(model, view, control);
    }

    public void handle(MouseEvent event) {
        // if mouse event capture is disabled in the model, just return
        if (!model.isCaptureMouseEvent()) return;


        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();

        QuixoBoard board = gameStage.getBoard();

        int[] coordCube = new int[1];

        List<Point> validCells = board.computeValidCells(true, coordCube, model);


        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(),event.getSceneY());
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);
        // for debug, uncomment next instructions to display x,y and elements at that postio
        Logger.debug("click in "+event.getSceneX()+","+event.getSceneY());
        int[] coordClick = {(int) event.getSceneX(), (int) event.getSceneY()};
        for(GameElement element : list) {
            Logger.debug(element.toString());
        }
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();

//        for(GameElement element : list) {
//            if (element.getType() == ElementTypes.getType("cube")) {
//                Cube cube = (Cube) element;
//                element.toggleSelected();
//                cube.setFace(1);
//
//                ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", 0, 0, AnimationTypes.MOVE_LINEARPROP, 10);
//                actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
////                stageModel.unselectAll();
//                stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
//                ActionPlayer play = new ActionPlayer(model, control, actions);
//                play.start();
//            }
//        }

        if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDCUBE) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("cube")) {
                    System.out.println("Jsuis renttré dans ce if");
                    Cube cube = (Cube)element;

                    // parcourir toutes les validCells
                    for (int i = 0; i<validCells.size(); i++){
                        //vérifier si le cube clické est dans validCells
                        if (coordClick[0] == (int)validCells.get(i).getX() && coordClick[1] == (int)validCells.get(i).getY()){
                            System.out.println("Je joue le coup car il est valide");
                            ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", 0, 0, AnimationTypes.MOVE_LINEARPROP, 10);
                            actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
                            stageModel.unselectAll();
                            stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                            ActionPlayer play = new ActionPlayer(model, control, actions);
                            play.start();

                        }
                    }
                    System.out.println("Ce coup est invalide");

//                    if (coordClick) {
//
//                        element.toggleSelected();
//                        stageModel.setState(QuixoStageModel.STATE_SELECTEDDEST);
//                        System.out.println("Jsuis renttré ici aussi");
//                        return; // do not allow another element to be selected
//                    }
                }
            }
        }



//        if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDCUBE) {
//            for (GameElement element : list) {
//                if (element.getType() == ElementTypes.getType("cube")) {
//                    System.out.println("Jsuis renttré dans ce if");
//                    Cube cube = (Cube)element;
//                    // check if color of the pawn corresponds to the current player id
////                    if (cube.getColor() == model.getIdPlayer()) {
//                    if () {
//                        element.toggleSelected();
//                        stageModel.setState(QuixoStageModel.STATE_SELECTEDDEST);
//                        System.out.println("Jsuis renttré ici aussi");
//                        return; // do not allow another element to be selected
//                    }
//                }
//            }
//        }
        else if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDDEST) {
            // first check if the click is on the current selected pawn. In this case, unselect it
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                    return;
                }
            }
            // secondly, search if the board has been clicked. If not just return
            boolean boardClicked = false;
            for (GameElement element : list) {
                if (element == stageModel.getBoard()) {
                    boardClicked = true; break;
                }
            }
            if (!boardClicked) return;
            // get the board, pot,  and the selected pawn to simplify code in the following
            // by default get black pot
            QuixoPawnPot pot = stageModel.getRedPot();
            // but if it's player2 that plays, get red pot
            GameElement cube = model.getSelected().get(0);

            // thirdly, get the clicked cell in the 3x3 board
            GridLook lookBoard = (GridLook) control.getElementLook(board);
            int[] dest = lookBoard.getCellFromSceneLocation(clic);
            System.out.println("int[] dest : "+Arrays.toString(dest));
            // get the cell in the pot that owns the selected pawn
            int[] from = pot.getElementCell(cube);
            System.out.println("int[] from : "+Arrays.toString(dest));

            Logger.debug("try to move pawn from pot "+from[0]+","+from[1]+ " to board "+ dest[0]+","+dest[1]);
            // if the destination cell is valid for for the selected pawn
            if (board.canReachCell(dest[0], dest[1])) {

                ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "quixoboard", dest[0], dest[1], AnimationTypes.MOVE_LINEARPROP, 10);
                actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
                stageModel.unselectAll();
                stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                ActionPlayer play = new ActionPlayer(model, control, actions);
                play.start();
            }
        }
    }
}
