package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.ContainerElement;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;
import view.QuixoView;
import view.WinnerScreen;

public class QuixoController extends Controller {

    QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
    QuixoStageModel quixoStageModel;
    QuixoView quixoView;
    int idWinner;

    public QuixoController(Model model, View view) {
        super(model, view);
        setControlKey(new QuixoKeyController(model, view, this));
        setControlMouse(new QuixoMouseController(model, view, this));
        setControlAction (new QuixoActionController(model, view, this));
        quixoStageModel = new QuixoStageModel("quixostagemodel", model);

        quixoView = (QuixoView) view;
    }

    public void endOfTurn() {

        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        QuixoBoard board = gameStage.getBoard();

        idWinner = quixoStageModel.setupCallbacks(board);
        if (idWinner != -1) {
            System.out.println("Affichage de la fenêtre de victoire pour le joueur " + idWinner);
            WinnerScreen winnerScreen = new WinnerScreen(gameStage, this, quixoView);
            winnerScreen.initDialog();
            return;
        }
        // use the default method to compute next player
        model.setNextPlayer();
        // get the new player
        Player p = model.getCurrentPlayer();
        // change the text of the TextElement
        QuixoStageModel stageModel = (QuixoStageModel) model.getGameStage();
        stageModel.getPlayerName().setText(p.getName());
        if (p.getType() == Player.COMPUTER) {
            Logger.debug("COMPUTER PLAYS");
            QuixoDecider2 decider = new QuixoDecider2(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            Logger.debug("PLAYER PLAYS");
        }
    }


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
                    actions = ActionFactory.generatePutInContainer(this, model, cubeBoard, "quixoboard", i + 1, insertionCol);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            } else {
                System.out.println("Déplacement colonne vers le haut");
                for (int i = insertionRow; i > fromRow; i--) {
                    Cube cubeBoard = (Cube) board.getElement(i, insertionCol);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(this, model, cubeBoard, "quixoboard", i - 1, insertionCol);
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
                    actions = ActionFactory.generatePutInContainer(this, model, cubeBoard, "quixoboard", insertionRow, i + 1);
                    ActionPlayer play = new ActionPlayer(model, this, actions);
                    play.start();
                }
            } else {
                System.out.println("Déplacement ligne vers la gauche");
                for (int i = insertionCol; i > fromCol; i--) {
                    Cube cubeBoard = (Cube) board.getElement(insertionRow, i);
                    if (cubeBoard == null)
                        continue;
                    actions = ActionFactory.generatePutInContainer(this, model, cubeBoard, "quixoboard", insertionRow, i - 1);
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

        actions = ActionFactory.generatePutInContainer(this,model, cube, "quixoboard", insertionRow, insertionCol);
        actions.setDoEndOfTurn(true); // Finir le tour après cette action

        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
    }


}
