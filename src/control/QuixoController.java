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

public class QuixoController extends Controller {

    BufferedReader consoleIn;
    int currentPlayer;
    int[] coordCube = new int[2];
    ContainerElement board;
    QuixoStageModel quixoStageModel;
    QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();



    public QuixoController(Model model, View view) {
        super(model, view);
        currentPlayer = 1;
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
            System.out.println("Joueur actuel : " + currentPlayer);
            if (model.getCurrentPlayer().getType() == Player.COMPUTER) {
                playTurn(null);
            } else {
                playTurn(false);
                update();
                playTurn(true);
            }

            setNextPlayer();
            endOfTurn();
            update();
        }
        endGame();
    }

    private void playTurn(Boolean isSecondeMove) {
        board = gameStage.getBoard();
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            QuixoDecider2 decider = new QuixoDecider2(model, this);
            ActionPlayer playDecider = new ActionPlayer(model, this, decider, null);
            playDecider.start();

        } else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName() + " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 2) {
                        if (!isSecondeMove) {
                            ok = analyseAndPlay(line);
                        }
                        else {
                            ok = analyseAndPlay2(line);
                        }

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

        quixoStageModel.setupCallbacks(board, currentPlayer);

        model.setNextPlayer();
        // get the new player to display its name
        Player p = model.getCurrentPlayer();
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());

    }

    private boolean analyseAndPlay(String line) {

        // get the ccords in the board
        int col = (int) (line.charAt(0) - 'A');
        int row = (int) (line.charAt(1) - '1');

//        System.out.println("row = " + row + " col = " + col + "----------------------------------------------------");

        // check if the pawn is still in its pot
        board = null;


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
//        System.out.println(coupsValides.toString());
        if(!coupsValides.contains(coup))
            return false;


        ActionList actions = ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0);


        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();

        setColorCoups(true);
        return true;
    }


    private void setColorCoups(boolean setJouable) {
        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        QuixoBoard board = gameStage.getBoard();

        List<Point> coupsPossibles = board.computeValidCells(false, coordCube, model);
        System.out.println(coupsPossibles.toString());
        for (int i = 0; i < coupsPossibles.size(); i++) {
            Point p = coupsPossibles.get(i);
            Cube coupsValideColores = (Cube) board.getElement(p.x, p.y);
            if (setJouable)
                coupsValideColores.setJouable();
            else
                coupsValideColores.resetJouable();
        }
    }


    private boolean analyseAndPlay2(String line) {
        setColorCoups(false);

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
//            System.out.println("Dans le reach cell");
            return false;
        }

        //Regarder si le coup actuel est dans la liste des bons coups fournie par computeValidCells()
        List<Point> coupsValides = board.computeValidCells(false, coordCube, model);
        System.out.println(coupsValides.toString());
        if(!coupsValides.contains(coup))
            return false;


        mooveSequenceCube(row, col, coordCube[1], coordCube[0], true);

        return true;
    }



    // S'occupe de déplacer les cubes 1 par 1 et ajoute le cube choisi en dernier
    public void mooveSequenceCube(int insertionRow, int insertionCol, int fromRow, int fromCol, boolean human) {
        // insertionRow = ligne où sera inséré le cube du joueur après le décalage
        // insertionCol = colonne où sera inséré le cube du joueur après le décalage
        // fromRow = ligne du cube le plus éloigné, celui qui sera déplacé en premier
        // fromCol = colonne du cube le plus éloigné, celui qui sera déplacé en premier
        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        QuixoBoard board = gameStage.getBoard();

        Cube cube = null;
        if (human) {
            ContainerElement pot = gameStage.getRedPot();
            cube = (Cube) pot.getElement(0, 0);
        } else {
            cube = (Cube) board.getElement(fromRow, fromCol);
        }



        ActionList actions = null;

        if (insertionCol == fromCol) {
            if (insertionRow < fromRow) {
                System.out.println("Déplacement colonne vers le bas");
                for (int i = insertionRow; i < fromRow; i++) {
                    Cube cubeBoard = (Cube) board.getElement(i, insertionCol);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", i + 1, insertionCol);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            } else {
                System.out.println("Déplacement colonne vers le haut");
                for (int i = insertionRow; i > fromRow; i--) {
                    Cube cubeBoard = (Cube) board.getElement(i, insertionCol);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", i - 1, insertionCol);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            }
        }

        if (insertionRow == fromRow) {
            if (insertionCol < fromCol) {
                System.out.println("Déplacement ligne vers la droite");
                for (int i = insertionCol; i < fromCol; i++) {
                    Cube cubeBoard = (Cube) board.getElement(insertionRow, i);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", insertionRow, i + 1);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            } else {
               System.out.println("Déplacement ligne vers la gauche");
                for (int i = insertionCol; i > fromCol; i--) {
                    Cube cubeBoard = (Cube) board.getElement(insertionRow, i);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(model, cubeBoard, "quixoboard", insertionRow, i - 1);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            }
        }

        // Mise à jour de la face du cube final selon le joueur
        if (model.getIdPlayer() == 0)
            cube.setFace(1);
        else
            cube.setFace(2);

        actions = ActionFactory.generatePutInContainer(model, cube, "quixoboard", insertionRow, insertionCol);
        actions.setDoEndOfTurn(true); // Finir le tour après cette action

        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
    }

    public void setNextPlayer(){
        if (currentPlayer == 1)
            currentPlayer = 2;
        else
            currentPlayer = 1;
    }


}