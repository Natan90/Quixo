package control;

import boardifier.model.Model;
import boardifier.view.View;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.RadioButton;
import view.DialogView;

public class QuixoChoiceController implements  EventHandler<ActionEvent> {

    private Model model;
    private DialogView dialogView;
    private View view;


    public QuixoChoiceController(Model model, DialogView dialogView) {
        this.model = model;
        this.dialogView = dialogView;
    }

    @Override
    public void handle(ActionEvent event) {
        System.out.println("Action event triggered: " + event.getSource());
        nameDisplay(dialogView.getGameMode());
    }

    // Change la vue pour le mode choisis et rend le bouton disable
    public void nameDisplay(int choice) {
        if (choice == 1) {
            System.out.println("joueur contre joueur");
            dialogView.showJcJ();
            dialogView.getApplyButton().setDisable(true);
        } else if (choice == 2) {
            System.out.println("joueur contre bot");
            dialogView.showJcB();
            dialogView.getApplyButton().setDisable(true);
        } else {
            System.out.println("bot contre bot");
            dialogView.showDefault();
        }
    }

    // rend le bouton enable quand on clique su un radioButton (il y a un listener sur le toggleGroup des radioButton)
    public void onGameModeChanged() {
        dialogView.getApplyButton().setDisable(false);
    }

}
