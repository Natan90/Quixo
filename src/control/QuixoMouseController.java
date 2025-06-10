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
        System.out.println(pot.getX() + " getX");

        Coord2D coordPot = new Coord2D();
        System.out.println(coordPot.getX() + " " + coordPot.getY() + " coordPot");

        int[] tabPot = {(int) coordPot.getX(), (int) coordPot.getY()};

        System.out.println(Arrays.toString(tabPot) + " tabPot");


        List<Point> validCells = board.computeValidCells(true, coordCube, model);


        // get the clic x,y in the whole scene (this includes the menu bar if it exists)
        Coord2D clic = new Coord2D(event.getSceneX(), event.getSceneY());
//        System.out.println(quixoBoardLook.getCellFromSceneLocation + " " + clic.getY());
        QuixoBoardLook lookBoard = (QuixoBoardLook) control.getElementLook(board);
        int[] dest = lookBoard.getCellFromSceneLocation(clic);
        System.out.println(dest);
        // get elements at that position
        List<GameElement> list = control.elementsAt(clic);
        // for debug, uncomment next instructions to display x,y and elements at that postio
        Logger.debug("click in " + event.getSceneX() + "," + event.getSceneY());
        int[] coordClick = {(int) event.getSceneX(), (int) event.getSceneY()};
        for (GameElement element : list) {
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
                    for (int i = 0; i < validCells.size(); i++) {
                        Point valid = validCells.get(i);
                        if (dest != null && dest[0] == (int) valid.getX() && dest[1] == (int) valid.getY()) {
                            element.toggleSelected();
                            stageModel.setState(QuixoStageModel.STATE_SELECTEDDEST);

                            System.out.println(cube.getX() + " " + cube.getY() + " coordonnées du cube");

                            cube.setFace(2);
                            System.out.println(cube.getFace());
                            ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", 0, 0, AnimationTypes.MOVE_LINEARPROP, 12);
                            stageModel.unselectAll();
                            actions.setDoEndOfTurn(false);
//                            board.updateLastCubePosition(dest[0], dest[1]);
                            ActionPlayer play = new ActionPlayer(model, control, actions);
                            play.start();


                            System.out.println("J'ai appuyé sur le cube " + Arrays.toString(dest));

                            coordCube[0] = dest[0];
                            coordCube[1] = dest[1];
                            return; // do not allow another element to be selected
                        }
                    }
                }
                System.out.println("TEST sélectionné");
            }
        } else if (stageModel.getState() == QuixoStageModel.STATE_SELECTEDDEST) {
            Cube cube = (Cube) pot.getElement(0, 0);
            QuixoController quixoController = (QuixoController) control;

//            for (int i = 0; i < list.size(); i++) {
//                GameElement element = list.get(i);
//                if (element.getType() == ElementTypes.getType("cube")) {
//                    coordCube[0] = dest[0];
//                    coordCube[1] = (int) list.get(i).getY();
//                }
//            }


            System.out.println("dest[0] : " + dest[0] + ", dest[1] :" + dest[1]);

            validCells = board.computeValidCells(false, coordCube, model);

            for (Point valid : validCells) {
                if (dest != null && dest[0] == (int) valid.getX() && dest[1] == (int) valid.getY()) {
                    quixoController.mooveSequenceCube(dest[0], dest[1], coordCube[0], coordCube[1], true);
                    stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                    return;
                }
            }
            System.out.println("TEST pas sélectionne");
        }


    }
}



