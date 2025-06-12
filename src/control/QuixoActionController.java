package control;

import boardifier.control.Controller;
import boardifier.control.ControllerAction;
import boardifier.model.*;
import boardifier.view.RootPane;
import boardifier.view.View;
import javafx.event.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import model.QuixoStageModel;
import view.DialogView;
import view.QuixoView;

import javax.swing.text.html.Option;
import java.util.Objects;
import java.util.Optional;

/**
 * A basic action controller that only manages menu actions
 * Action events are mostly generated when there are user interactions with widgets like
 * buttons, checkboxes, menus, ...
 */
public class QuixoActionController extends ControllerAction implements EventHandler<ActionEvent> {

    // to avoid lots of casts, create an attribute that matches the instance type.
    private QuixoView quixoView;
    private QuixoController quixoController;

    public QuixoActionController(Model model, View view, Controller control) {
        super(model, view, control);
        // take the view parameter ot define a local view attribute with the real instance type, i.e. BasicView.
        quixoView = (QuixoView) view;
        quixoController = (QuixoController) control;

        // set handlers dedicated to menu items
        setMenuHandlers();

        // If needed, set the general handler for widgets that may be included within the scene.
        // In this case, the current gamestage view must be retrieved and casted to the right type
        // in order to have an access to the widgets, and finally use setOnAction(this).
        // For example, assuming the current gamestage view is an instance of MyGameStageView, which
        // creates a Button myButton :
        // ((MyGameStageView)view.getCurrentGameStageView()).getMyButton().setOnAction(this).

    }

    private void setMenuHandlers() {
        quixoView.getMenuHelp().setOnAction(e -> {
                DialogView dialogView = new DialogView(model);
                    Optional<ButtonType> result = dialogView.initDialog();

                    if (result.isPresent() && result.get() == dialogView.getButtonTypeJouer()) {
                        dialogView.initVBox();

                    }

                });

       // set event handler on the MenuStart item
        quixoView.getMenuStart().setOnAction(e -> {
            try {
//                control.startGame();
                DialogView dialogView = new DialogView(model);

                dialogView.initVBox();
                Optional<ButtonType> result = dialogView.initDialog();
                if (result.isPresent() && result.get() == dialogView.getButtonTypeJouer()) {
                    int mode = dialogView.getGameMode();
                    if (model.isStageStarted()) {
                        System.out.println("Stop previous game");
                        control.stopStage();
                    }
                    model.getPlayers().clear();
                    model.reset();

                    if (mode == 1) {
                        System.out.println("joueur contre joueur");
                        model.addHumanPlayer("player1");
                        model.addHumanPlayer("player2");
                        if (!Objects.equals(dialogView.getTextField1().getText(), "")) {
//                            Button button = (Button) dialogView.getDialog().getDialogPane().lookupButton(dialogView.getButtonTypeJouer());
//                            button.setDisable(true);
                            model.getPlayers().getFirst().setName(dialogView.getTextField1().getText());
                        }
                        if (!Objects.equals(dialogView.getTextField2().getText(), "")) {
//                            Button button = (Button) dialogView.getDialog().getDialogPane().lookupButton(dialogView.getButtonTypeJouer());
//                            button.setDisable(true);
                            model.getPlayers().get(1).setName(dialogView.getTextField2().getText());
                        }
                    } else if (mode == 2) {
                        System.out.println("joueur contre bot");
                        model.addHumanPlayer("player");
                        model.addComputerPlayer("computer");
                        if (!Objects.equals(dialogView.getTextField1().getText(), ""))
                            model.getPlayers().getFirst().setName(dialogView.getTextField1().getText());

                        int difficulty = dialogView.getBotDifficulty();
                        System.out.println("bot difficulty = " + difficulty);
                        quixoController.setBotDifficulty(difficulty);

                    } else {
                        System.out.println("bot contre bot");
                        model.addComputerPlayer("computer1");
                        model.addComputerPlayer("computer2");
                    }

                    QuixoStageModel stageModel = new QuixoStageModel("quixo", model);
                    model.startGame(stageModel);

                    quixoView.resetGameStageView();
                    quixoView.setView(quixoView.getGameStageView());

                    control.startGame();
                }
            }
            catch(Exception err) {
                System.err.println(err.getMessage());
                System.exit(1);
            }
        });
        // set event handler on the MenuIntro item
        quixoView.getMenuIntro().setOnAction(e -> {
            control.stopGame();
            quixoView.resetView();
        });
        // set event handler on the MenuQuit item
        quixoView.getMenuQuit().setOnAction(e -> {
            System.exit(0);
        });
    }

    /**
     * The general handler for action events.
     * this handler should be used if the code to process a particular action event is too long
     * to fit in an arrow function (like with menu items above). In this case, this handler must be
     * associated to a widget w, by calling w.setOnAction(this) (see constructor).
     *
     * @param event An action event generated by a widget of the scene.
     */
    public void handle(ActionEvent event) {

        if (!model.isCaptureActionEvent()) return;
    }
}

