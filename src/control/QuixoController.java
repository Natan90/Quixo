package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.*;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.Cube;
import model.QuixoStageModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

        //verifier que l'utilisateur a bien choisi une case sur l'exterieur du plateau
        if (col > 0 && col < 4 && row > 0 && row < 4)
            return false;

        System.out.println("row = " + row + " col = " + col + "----------------------------------------------------");

        // check if the pawn is still in its pot
        board = null;
        board = gameStage.getBoard();

//        if (pot.isEmptyAt(pawnIndex,0)) return false;

        //recuperer le cube choisi dans le plateau
        Cube cube = (Cube) board.getElement(row, col);
        coordCube[0] = col;
        coordCube[1] = row;

        //regarder si le joueur n'a pas choisi un cube de l'autre joueur
        if ((cube.getFace() == 2 && model.getIdPlayer() == 0) || (cube.getFace() == 1 && model.getIdPlayer() == 1)) {
            return false;
        }
        // compute valid cells for the chosen pawn
        int cubeIndex = (col + 1) * (row + 1);

        //bouger le pion choisi -> réafficher le plateau

//        ActionList actions = ActionFactory.generatePutInContainer(model, cube, "cubepot", 1, 1);
//        ActionList actions = ActionFactory.generateRemoveFromContainer(model, cube);
        ActionList actions = ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0);


        //maintenant il faut appeler la méthode pour setValidCells
        //demander un deuxieme coup a l'utilisateur
        //verifier si son coup est valide
        //si oui, jouer le coup, afficher le plateau du jeu et mettre fin au tour du joueur
//        gameStage.getBoard().setValidCells(cubeIndex);

        if (!gameStage.getBoard().canReachCell(row, col)) {
            return false;
        }


        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }

    private boolean analyseAndPlay2(String line) {
        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        // get the ccords in the board
        int col = (int) (line.charAt(0) - 'A');
        int row = (int) (line.charAt(1) - '1');

        //verifier que l'utilisateur a bien choisi une case sur l'exterieur du plateau


//        ContainerElement board = null;

        ContainerElement board = gameStage.getBoard();

        ContainerElement pot = null;
        pot = gameStage.getRedPot();

        //recuperer le cube choisi dans le plateau
        Cube cube = (Cube) pot.getElement(0, 0);
        //regarder si le jo ueur n'a pas choisi un cube de l'autre joueur
//        if(cube.getFace() == 2 && model.getIdPlayer() == 1 || cube.getFace() == 1 && model.getIdPlayer() == 2) {
//            return false;
//        }
        // compute valid cells for the chosen pawn
//        int cubeIndex = (col+1)*(row+1) ;

        System.out.println("row =" + row + " col = " + col + " -------------------------------------------------------");

        //verifier si on est dans les 9 cubes du milieu
        if (col > 0 && col < 4 && row > 0 && row < 4) {
            System.out.println("Dans le carré du milieu");
            return false;
        }
        //verifier si on joue bien sur la ligne ou colonne correspondante
        if (col != coordCube[0] && row != coordCube[1]) {
            System.out.println("Dans la verif tableau coup bon");
            return false;
        }
        //verifier si c'est la meme position
        if (col == coordCube[0] && row == coordCube[1]) {
            System.out.println("Position exacte");
            return false;
        }
        //verifier si c'est la bonne saisie et comprise entre 1 et 3 inclus
        if ((col == coordCube[0] && row > 0 && row < 4) || (row == coordCube[1] && col > 0 && col < 4)) {
            System.out.println("dans le truc de baisé");
            return false;
        }


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


        if (!gameStage.getBoard().canReachCell(row, col)) {
            System.out.println("Dans le reach cell");
            return false;
        }


        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        return true;
    }

//    public boolean isWinning(ContainerElement board) {
//
//    }
}