package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.model.GameElement;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.Player;
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

    public QuixoController(Model model, View view) {
        super(model, view);
        firstPlayer = true;
    }

    /**
     * Defines what to do within the single stage of the single party
     * It is pretty straight forward to write :
     */
    public void stageLoop() {
        consoleIn = new BufferedReader(new InputStreamReader(System.in));
        update();
        while(! model.isEndStage()) {
            playTurn();
            endOfTurn();
            update();
        }
        endGame();
    }

    private void playTurn() {
        // get the new player
        Player p = model.getCurrentPlayer();
        if (p.getType() == Player.COMPUTER) {
            System.out.println("COMPUTER PLAYS");
            QuixoDecider decider = new QuixoDecider(model,this);
            ActionPlayer play = new ActionPlayer(model, this, decider, null);
            play.start();
        }
        else {
            boolean ok = false;
            while (!ok) {
                System.out.print(p.getName()+ " > ");
                try {
                    String line = consoleIn.readLine();
                    if (line.length() == 2) {
                        ok = analyseAndPlay(line);
                    }
                    if (!ok) {
                        System.out.println("incorrect instruction. retry !");
                    }
                }
                catch(IOException e) {}
            }
        }
    }

    public void endOfTurn() {
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
        if(col > 0 && col < 3 && row > 0 && row < 3)
            return false;

        // check if the pawn is still in its pot
        ContainerElement pot = null;
        pot = gameStage.getBoard();

//        if (pot.isEmptyAt(pawnIndex,0)) return false;

        //recuperer le cube choisi dans le plateau
        Cube cube = (Cube) pot.getElement(col, row);
        //regarder si le joueur n'a pas choisi un cube de l'autre joueur
        if(cube.getFace() == 2 && model.getIdPlayer() == 1 || cube.getFace() == 1 && model.getIdPlayer() == 2) {
            return false;
        }
        // compute valid cells for the chosen pawn
        int cubeIndex = (col+1)*(row+1) ;

        //bouger le pion choisi -> réafficher le plateau
        ActionList actions = ActionFactory.generatePutInContainer(model, cube, "quixoboard", 10, 10);
        //maintenant il faut appeler la méthode pour setValidCells
        //demander un deuxieme coup a l'utilisateur
        //verifier si son coup est valide
        //si oui, jouer le coup, afficher le plateau du jeu et mettre fin au tour du joueur


//        gameStage.getBoard().setValidCells(cubeIndex);
        if (!gameStage.getBoard().canReachCell(row,col)) return false;


        actions.setDoEndOfTurn(false); // after playing this action list, it will be the end of turn for current player.
        ActionPlayer play = new ActionPlayer(model, this, actions);
        play.start();
        return true;
    }
}
