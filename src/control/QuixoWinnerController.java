package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonType;
import model.QuixoStageModel;
import view.QuixoView;
import view.WinnerScreen;

import java.util.Optional;

public class QuixoWinnerController implements EventHandler<ActionEvent> {

    private QuixoStageModel quixoStageModel;
    private WinnerScreen winnerScreen;
    private Controller controller;
    private QuixoView quixoView;

    public QuixoWinnerController(QuixoStageModel quixoStageModel, WinnerScreen winnerScreen, Controller controller, QuixoView quixoView) {
        this.quixoStageModel = quixoStageModel;
        this.winnerScreen = winnerScreen;
        this.controller = controller;
        this.quixoView = quixoView;
    }

    @Override
    public void handle(ActionEvent actionEvent) {
//        Optional<ButtonType> result = winnerScreen.getResult();
//        if (result.isPresent() && result.get() == winnerScreen.getQuitter()) {
//            System.exit(0);
//            System.out.println("quitter");
//        } else if (result.isPresent() && result.get() == winnerScreen.getRecommencer()) {
//            controller.stopGame();
//            quixoView.resetView();
//            System.out.println("recommencer");
//        }
    }

    public void handleButton(ButtonType buttonType) {
        if (buttonType == winnerScreen.getQuitter()) {
            System.exit(0);
        } else if (buttonType == winnerScreen.getRecommencer()) {
            controller.stopGame();
            quixoView.resetView();
        }
    }



}
