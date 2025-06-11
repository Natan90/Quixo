package control;

import boardifier.model.Model;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import view.DialogView;

public class QuixoChoiceController implements  EventHandler<ActionEvent> {

    private Model model;
    private DialogView dialogView;
    private QuixoController quixoController;


    public QuixoChoiceController(Model model, DialogView dialogView) {
        this.model = model;
        this.dialogView = dialogView;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Action event triggered: " + event.getSource());
        nameDisplay();
    }

    private int getSelectedChoice(RadioButton radioButtonChoice1, RadioButton radioButtonChoice2) {
        if (radioButtonChoice1.isSelected()) return 1;
        if (radioButtonChoice2.isSelected()) return 2;
        return 3;
    }

    public void nameDisplay() {
        int choice = getSelectedChoice(dialogView.getRadioButtonChoice1(), dialogView.getRadioButtonChoice2());
        if (choice == 1) {
            System.out.println("joueur contre joueur");
            dialogView.showJcJ();

            model.reset();
            model.addHumanPlayer("player1");
            model.addHumanPlayer("player2");
        } else if (choice == 2) {
            System.out.println("joueur contre bot");
            dialogView.showJcB();
            difficultDisplay();

            model.reset();
            model.addHumanPlayer("player");
            model.addComputerPlayer("computer");
        } else {
            System.out.println("bot contre bot");
            dialogView.showDefault();
//            dialogView.showBcB();         Même vue si joueur contre joueur ou joueur contre bot

            model.reset();
            model.addComputerPlayer("computer1");
            model.addComputerPlayer("computer2");
        }

    }

    public void difficultDisplay() {
        int botChoice = getSelectedChoice(dialogView.getRadioButtonBot1(), dialogView.getRadioButtonBot2());
//        quixoController = new QuixoController(model, dialogView);

        if (botChoice == 1) {
            System.out.println("jouer le bot facile");
            dialogView.add1TextField();
            dialogView.addBot();


        }
        else {
            System.out.println("jouer le bot dur");
        }
    }
}
