package view;

import boardifier.control.Controller;
import control.QuixoWinnerController;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import model.QuixoStageModel;

import java.util.Optional;

public class WinnerScreen {

    private Dialog<ButtonType> dialog;
    private ButtonType quitter, recommencer;
    private VBox vBox;
    private HBox hBox, hBox2;
    private Label lMessageWin, lNamePlayer;
    private Optional<ButtonType> result;

    private QuixoStageModel model;
    private QuixoWinnerController quixoWinnerController;


    public WinnerScreen(QuixoStageModel model, Controller controller, QuixoView quixoView) {
        this.model = model;
        this.quixoWinnerController = new QuixoWinnerController(model, this, controller, quixoView);
    }

    public void initDialog() {
        dialog = new Dialog<>();

        quitter = new ButtonType("Leave");
        recommencer = new ButtonType("Start again");

        dialog.getDialogPane().getButtonTypes().addAll(quitter, recommencer);
        dialog.setTitle("Winner");
        dialog.initModality(Modality.APPLICATION_MODAL);

        lMessageWin = new Label("The winner of the game is : ");
        lNamePlayer = new Label(model.getCurrentPlayerName());

        vBox = new VBox();
        vBox.setSpacing(10);

        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(lMessageWin);

        hBox2 = new HBox();
        hBox2.setAlignment(Pos.CENTER);
        hBox2.getChildren().addAll(lNamePlayer);

        vBox.getChildren().addAll(hBox, hBox2);

        dialog.getDialogPane().setContent(vBox);

        addListeners();
        result = dialog.showAndWait();
    }

    public void addListeners() {
        Button quitterButton = (Button) dialog.getDialogPane().lookupButton(quitter);
        Button recommencerButton = (Button) dialog.getDialogPane().lookupButton(recommencer);

        quitterButton.setOnAction(event -> quixoWinnerController.handleButton(quitter));
        recommencerButton.setOnAction(event -> quixoWinnerController.handleButton(recommencer));
    }


    public ButtonType getQuitter() {
        return quitter;
    }

    public ButtonType getRecommencer() {
        return recommencer;
    }




}