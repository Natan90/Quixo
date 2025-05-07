package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;

import java.awt.*;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class QuixoDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider(Model model, Controller control) {
        super(model, control);
    }

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel)model.getGameStage();
        QuixoBoard board = stage.getBoard(); // get the board
        QuixoPawnPot pot = null; // the pot where to take a pawn
        GameElement pawn = null; // the pawn that is moved
        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board

        int[] coordCube = new int[2];

        List<Point> valid = board.computeValidCells(true, coordCube, model);
        //regarder si on ne peut pas aligner 5 cubes
        // Si non, essayer de creer la plus grande serie de cube possible
        // Si il n'y a pas de possibilité de faire une ligne de 3,


        ActionList actions = ActionFactory.generatePutInContainer( model, null, "quixoboard", 0, 0);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

        return actions;
    }
}
