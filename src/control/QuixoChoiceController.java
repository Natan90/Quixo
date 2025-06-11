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
        nameDisplay(dialogView.getGameMode());
    }


    public void nameDisplay(int choice) {
        if (choice == 1) {
            System.out.println("joueur contre joueur");
            dialogView.showJcJ();

        } else if (choice == 2) {
            System.out.println("joueur contre bot");
            dialogView.showJcB();
            difficultDisplay();

        } else {
            System.out.println("bot contre bot");
            dialogView.showDefault();

        }

    }

    public void difficultDisplay() {
        int botChoice = dialogView.getBotDifficulty();
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
