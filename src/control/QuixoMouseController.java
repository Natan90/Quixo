package control;

import boardifier.control.*;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.model.action.GameAction;
import boardifier.model.action.PutInContainerAction;
import boardifier.model.action.RemoveFromContainerAction;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.ContainerLook;
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
import view.QuixoBoardLook;
import view.RedPawnPotLook;

import java.awt.*;
import java.lang.reflect.Array;
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
        QuixoPawnPot pot = gameStage.getRedPot();

        int[] coordCube = {0, 0};

//        RedPawnPotLook potLook = (RedPawnPotLook) control.getElementLook(pot);

        GridLook look = (GridLook) control.getElementLook(pot);
        Coord2D coordPot = look.getRootPaneLocationForLookFromCell((ElementLook) look, 0, 0);

        ElementLook elementLook = control.getElementLook(cube);
        // get the container look of the destination container
        ContainerLook containerLook = (ContainerLook) control.getElementLook(containerDest);
        // get the location of the element look within this destination look, if placed in rowDest, colDest
        Coord2D center = containerLook.getRootPaneLocationForLookFromCell(elementLook, rowDest, colDest);

//        Coord2D coordPot = new Coord2D(potLook.);
        System.out.println(coordPot.getX() + " " + coordPot.getY()+ " coordPot");

        int[] tabPot = {(int) coordPot.getX() ,(int) coordPot.getY()};

        System.out.println(Arrays.toString(tabPot) + " tabPot");


        List<Point> validCells = board.computeValidCells(true, coordCube, model);


        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(),event.getSceneY());
//        System.out.println(quixoBoardLook.getCellFromSceneLocation + " " + clic.getY());
        QuixoBoardLook lookBoard = (QuixoBoardLook) control.getElementLook(board);
        int[] dest = lookBoard.getCellFromSceneLocation(clic);
        System.out.println(dest);
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);
        // for debug, uncomment next instructions to display x,y and elements at that postio
        Logger.debug("click in "+event.getSceneX()+","+event.getSceneY());
        int[] coordClick = {(int) event.getSceneX(), (int) event.getSceneY()};
        for(GameElement element : list) {
            Logger.debug(element.toString());
        }
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();

//        for (GameElement element : list){
//            if(element.getType() == "cube")
//        }

        // if quand le cube est déja sélctionné -> placer le cube
        if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDCUBE) {
            for (GameElement element : list) {
                if (element.getType() == ElementTypes.getType("cube")) {
                    Cube cube = (Cube) element;
                    // check if color of the pawn corresponds to the current player id
                    for (int i = 0; i<validCells.size();i++){
                        Point valid = validCells.get(i);
                        if (dest != null && dest[0] == valid.getX() && dest[1] == valid.getY()) {
                            element.toggleSelected();
                            stageModel.setState(QuixoStageModel.STATE_SELECTEDDEST);

                            System.out.println(cube.getX()+ " "+ cube.getY()+ " coordonnées du cube");
                            ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", tabPot[0],tabPot[0], AnimationTypes.MOVE_LINEARPROP, 10);
                            stageModel.unselectAll();
//                            board.updateLastCubePosition(dest[0], dest[1]);
                            ActionPlayer play = new ActionPlayer(model, control, actions);
                            play.start();

                            System.out.println(cube.getX()+ " "+ cube.getY());


                            System.out.println("J'ai appuyé sur le cube " + Arrays.toString(dest));

                            return; // do not allow another element to be selected
                        }
                    }
                }
                System.out.println("TEST sélectionné");
            }
        }else if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDDEST) {

            // first check if the click is on the current selected pawn. In this case, unselect it
            for (GameElement element : list) {
                if (element.isSelected()) {
                    element.toggleSelected();
                    stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                    return;
                }


            }


            stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
            System.out.println("TEST pas sélectionne");
        }



//        for(GameElement element : list) {
//            if (element.getType() == ElementTypes.getType("cube")) {
//                Cube cube = (Cube) element;
//                element.toggleSelected();
//                cube.setFace(1);
//
//                ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", 0, 0, AnimationTypes.MOVE_LINEARPROP, 10);
//                actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
//                stageModel.unselectAll();
//                stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
//                ActionPlayer play = new ActionPlayer(model, control, actions);
//                play.start();
//            }
//        }

    }
}
