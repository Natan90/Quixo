package control;

import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Logger;
import boardifier.model.GameException;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.view.View;
import model.QuixoStageModel;

public class QuixoController extends Controller {

    public QuixoController(Model model, View view) {
        super(model, view);
        setControlKey(new QuixoKeyController(model, view, this));
        setControlMouse(new QuixoMouseController(model, view, this));
        setControlAction (new QuixoActionController(model, view, this));
    }

    public void endOfTurn() {
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
}
