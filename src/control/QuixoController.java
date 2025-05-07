package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;

public class QuixoController extends Controller {

    BufferedReader consoleIn;
    boolean firstPlayer;
    int[] coordCube = new int[2];
    ContainerElement board;
    QuixoStageModel quixoStageModel;

    public QuixoController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
        quixoStageModel = new QuixoStageModel("quixostagemodel", model);
    }

    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     */
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        update();
        while (!model.isEndStage()) {
            playTurn(false);
            update();
            playTurn(true);
            endOfTurn();
            update();
        }
        endGame();
    }

    private void playTurn(boolean isSecondeMove) {
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            QuixoDecider decider = new QuixoDecider(model, this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 2) {
                        if (!isSecondeMove)
                            ok = analyseAndPlay(line);
                        else
                            ok = analyseAndPlay2(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    public void endOfTurn() {


        quixoStageModel.setupCallbacks(board);

        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());

    }

    private boolean analyseAndPlay(String line) {
        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        // get the ccords in the board
        int col = (int) (line.charAt(0) - 'A');
        int row = (int) (line.charAt(1) - '1');

        System.out.println("row = " + row + " col = " + col + "----------------------------------------------------");

        // check if the pawn is still in its pot
        board = null;
        board = gameStage.getBoard();

        //verifier si l'entree user est comprise entre 0 et 5
        if (!gameStage.getBoard().canReachCell(row, col)) {
            return false;
        }

        //recuperer le cube choisi dans le plateau
        Cube cube = (Cube) board.getElement(row, col);

        coordCube[0] = col;
        coordCube[1] = row;

        //regarder si le joueur n'a pas choisi un cube de l'autre joueur
        QuixoBoard board = gameStage.getBoard();

        Point coup = new Point(row, col);

        //Regarder si le coup actuel est dans la liste des bons coups fournie par computeValidCells()
        List<Point> coupsValides = board.computeValidCells(true, coordCube, model);
        System.out.println(coupsValides.toString());
        if(!coupsValides.contains(coup))
            return false;


        ActionList actions = ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0);


        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }

    private boolean analyseAndPlay2(String line) {
        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        // get the coords in the board
        int col = (int) (line.charAt(0) - 'A');
        int row = (int) (line.charAt(1) - '1');

        Point coup = new Point(row, col);


        QuixoBoard board = gameStage.getBoard();

        ContainerElement pot = null;
        pot = gameStage.getRedPot();

        //recuperer le cube choisi dans le plateau
        Cube cube = (Cube) pot.getElement(0, 0);

        if (!gameStage.getBoard().canReachCell(row, col)) {
            System.out.println("Dans le reach cell");
            return false;
        }

        //Regarder si le coup actuel est dans la liste des bons coups fournie par computeValidCells()
        List<Point> coupsValides = board.computeValidCells(false, coordCube, model);
        System.out.println(coupsValides.toString());

        if(!coupsValides.contains(coup))
            return false;


        ActionList actions = null;


        if (col == coordCube[0]) {
            if (row < coordCube[1]) {
                System.out.println("C'est une colone donc je déplace les ligne ++");
                for (int i = row; i < coordCube[1]; i++) {
                    Cube cubeBoard = (Cube) board.getElement(i, col);
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", i + 1, col);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                    System.out.println("i = " + i + "...............................................");
                }
            } else {
                System.out.println("C'est une colone donc je déplace les ligne --");
                for (int i = row; i > coordCube[1]; i--) {
                    Cube cubeBoard = (Cube) board.getElement(i, col);
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", i - 1, col);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                    System.out.println("i = " + i + "...............................................");

                }
            }
        }


        if (row == coordCube[1]) {
            if (col < coordCube[0]) {

                System.out.println("C'est une ligne donc je déplace les colonnes ++");
                for (int i = col; i < coordCube[0]; i++) {
                    Cube cubeBoard = (Cube) board.getElement(row, i);
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", row, i + 1);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                    System.out.println("i = " + i + "...............................................");

                }

            } else {
                System.out.println("C'est une ligne donc je déplace les colonnes --");
                for (int i = col; i > coordCube[0]; i--) {
                    Cube cubeBoard = (Cube) board.getElement(row, i);
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", row, i - 1);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                    System.out.println("i = " + i + "...............................................");

                }
            }
        }


        if (model.getIdPlayer() == 0)
            cube.setFace(1);
        else
            cube.setFace(2);

        actions = ActionFactory.generatePutInContainer(model, cube, "quixoboard", row, col);

        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }
}