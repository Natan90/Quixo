package control;

import boardifier.control.*;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import model.*;
import view.CoupsLook;
import view.QuixoBoardLook;
import view.QuixoStageView;
import view.TimerLook;

import java.awt.Point;
import java.util.Arrays;
import java.util.List;

public class QuixoMouseController extends ControllerMouse implements EventHandler<MouseEvent> {

    private int[] coordCube = {0, 0};
    private Timeline timeline;
    private TimerLook timerLook;
    private TimerElement timerElement;
    private CoupsLook coupsLook;
    private CoupsElement coupsElement;
    private int compteurTour = 0;
    private boolean isTimerRunning = false;


    public QuixoMouseController(Model model, View view, Controller control) {
        super(model, view, control);

    }

    @Override
    public void handle(MouseEvent event) {
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();
        timerElement = stageModel.getTimer();
        coupsElement = stageModel.getCoups();
        System.out.println("model.getIdPlayer() " + model.getIdPlayer());

        QuixoStageView stageView = (QuixoStageView) view.getGameStageView();
        timerLook = stageView.getTimerLook();
        coupsLook = stageView.getCoupsLook();

        toggleTimer();
        if (!model.isCaptureMouseEvent()) return;

//        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();
        QuixoBoard board = stageModel.getBoard();
        QuixoPawnPot pot = stageModel.getRedPot();

        Coord2D clic = new Coord2D(event.getSceneX(), event.getSceneY());
        System.out.println(clic.getX() + " " + clic.getY());

        QuixoBoardLook lookBoard = (QuixoBoardLook) control.getElementLook(board);
        int[] dest = lookBoard.getCellFromSceneLocation(clic);
        System.out.println(Arrays.toString(dest));

        List<GameElement> elementsAtClick = control.elementsAt(clic);
        Logger.debug("click in " + event.getSceneX() + "," + event.getSceneY());
        for (GameElement element : elementsAtClick) {
            Logger.debug(element.toString());
        }

        switch (stageModel.getState()) {
            case QuixoStageModel.STATE_SELECTEDDEST:
                if (clickedOnPot(pot, elementsAtClick)) {
                    System.out.println("Je clique sur le pot pour annuler mon coup");
                    cancelSelection(stageModel, pot, board);
                } else {
                    handleDestinationClick(stageModel, board, elementsAtClick, dest);
                }
                break;

            case QuixoStageModel.STATE_SELECTEDCUBE:
                handleCubeSelection(stageModel, board, elementsAtClick, dest);
                break;

            default:
                break;
        }
    }

    private boolean clickedOnPot(QuixoPawnPot pot, List<GameElement> elements) {
        for (GameElement element : elements) {
            if (element.getContainer() == pot) {
                return true;
            }
        }
        return false;
    }

    private void cancelSelection(QuixoStageModel stageModel, QuixoPawnPot pot, QuixoBoard board) {
        Cube cube = (Cube) pot.getElement(0, 0);
        ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "quixoboard", coordCube[1], coordCube[0]);
        ActionPlayer play = new ActionPlayer(model, control, actions);
        stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);

        List<Point> validCellsToReset = board.computeValidCells(false, coordCube, model);
        System.out.println(validCellsToReset.toString() + " validCellsToReset");

        for (Point p : validCellsToReset) {
            Cube cubeToReset = (Cube) board.getElement(p.x, p.y);
            if (cubeToReset != null)
                cubeToReset.setJouable(false);
        }
        play.start();
    }

    private void handleCubeSelection(QuixoStageModel stageModel, QuixoBoard board, List<GameElement> elements, int[] dest) {
        List<Point> validCells = board.computeValidCells(true, coordCube, model);

        for (GameElement element : elements) {
            if (element.getType() == ElementTypes.getType("cube")) {
                Cube cube = (Cube) element;
                for (Point valid : validCells) {
                    if (dest != null && dest[0] == valid.x && dest[1] == valid.y) {
                        element.toggleSelected();
                        stageModel.setState(QuixoStageModel.STATE_SELECTEDDEST);

                        System.out.println(cube.getX() + " " + cube.getY() + " coordonnées du cube");
                        System.out.println(cube.getFace());

                        ActionList actions = ActionFactory.generatePutInContainer(control, model, cube, "cubepot", 0, 0);
                        stageModel.unselectAll();
                        actions.setDoEndOfTurn(false);
                        ActionPlayer play = new ActionPlayer(model, control, actions);
                        play.start();

                        System.out.println("J'ai appuyé sur le cube " + Arrays.toString(dest));

                        coordCube[0] = dest[1];
                        coordCube[1] = dest[0];

                        System.out.println("coordCube : " + Arrays.toString(coordCube));

                        List<Point> validCells2 = board.computeValidCells(false, coordCube, model);
                        for (Point p : validCells2) {
                            Cube cubeJouable = (Cube) board.getElement(p.x, p.y);
                            if (cubeJouable != null)
                                cubeJouable.setJouable(true);
                        }
                        return;
                    }
                }
                System.out.println("TEST sélectionné");
            }
        }
    }

    private void handleDestinationClick(QuixoStageModel stageModel, QuixoBoard board, List<GameElement> elements, int[] dest) {
        QuixoController quixoController = (QuixoController) control;

        System.out.println("coordCube : " + Arrays.toString(coordCube));
        List<Point> validCells = board.computeValidCells(false, coordCube, model);

        System.out.println("validCells 2 " + validCells.toString());

        for (Point valid : validCells) {
            Cube cubeToReset = (Cube) board.getElement(valid.x, valid.y);
            if (cubeToReset != null)
                cubeToReset.setJouable(false);

            if (dest != null && dest[0] == valid.x && dest[1] == valid.y) {
                quixoController.mooveSequenceCube(dest[0], dest[1], coordCube[1], coordCube[0], true);
                stageModel.setState(QuixoStageModel.STATE_SELECTEDCUBE);
                timerLook.increment3(compteurTour);
                coupsLook.incrementeCoups();
                coupsLook.update();
                return;
            }
        }

    }

    public void toggleTimer() {
        if (!isTimerRunning) {
            startTimer();
        } else {
            stopTimer();
        }
        isTimerRunning = !isTimerRunning; // alterne l'état
    }


    public void startTimer() {
        System.out.println("compteur " + compteurTour);
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            timerElement.decrement();
            timerLook.update();
            System.out.println("Temps restant : " + timerElement.getFormattedTime());
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        compteurTour++;
    }

    public void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void updateTimer() {
        System.out.println("compteurtour " + compteurTour);
        if (compteurTour == 0) {
            timerLook.render();
        } else if (compteurTour % 2 == 0) {
            if (timerElement.getTimeLeft() >= 177) {
                timerElement.setTimeLeft(180);
            } else {
                timerElement.increment3();
            }
        }
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }
}
