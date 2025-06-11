package view;

import boardifier.model.Model;
import control.QuixoChoiceController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import model.QuixoStageModel;

import java.util.Optional;

public class DialogView {

    private ToggleGroup groupChoice, groupBot;
    private RadioButton radioButtonChoice1, radioButtonChoice2, radioButtonChoice3;
    private RadioButton radioButton1, radioButton2;
    private TextField textField1, textField2;
    private Button applique;
    private Dialog<ButtonType> dialog;
    private ButtonType cancel, ok;
    private VBox vBox;
    private HBox hbox, hbox2;
    private Label lTypeJeu, lTypeBot;
    private Separator separtor;
    private Region spacerVBox;


    private Model model;
    private QuixoChoiceController quixoChoiceController;


    public DialogView(Model model) {
        this.model = model;
        this.quixoChoiceController = new QuixoChoiceController(model, this);
//        super(model, stage, rootPane);
    }

    public Optional<ButtonType> initDialog() {
        dialog = new Dialog<>();

        cancel = new ButtonType("Cancel");
        ok = new ButtonType("Play");

        lTypeJeu = new Label("Choose your game type : ");
        lTypeBot = new Label("Choose the bot's difficulty : ");

        groupChoice = new ToggleGroup();
        radioButtonChoice1 = new RadioButton("Player vs Player");
        radioButtonChoice2 = new RadioButton("Player vs Bot");
        radioButtonChoice3 = new RadioButton("Bot vs Bot");
        radioButtonChoice1.setToggleGroup(groupChoice);
        radioButtonChoice2.setToggleGroup(groupChoice);
        radioButtonChoice3.setToggleGroup(groupChoice);
        radioButtonChoice1.setSelected(true);

        dialog.getDialogPane().getButtonTypes().addAll(cancel, ok);
        dialog.setTitle("Choose a level");
        dialog.initModality(Modality.APPLICATION_MODAL);

        separtor = new Separator();
        separtor.prefWidth(500);

        applique = new Button("Apply choices");


        hbox = new HBox();
        hbox2 = new HBox();

        hbox2.getChildren().addAll(radioButtonChoice1, radioButtonChoice2, radioButtonChoice3);
        spacerVBox = new Region();
        VBox.setVgrow(spacerVBox, Priority.ALWAYS);
        showDefault();



        dialog.getDialogPane().setContent(vBox);
        dialog.getDialogPane().setPrefSize(500, 300);


        Button jouerButton = (Button) dialog.getDialogPane().lookupButton(ok);
        Button cancelButton = (Button) dialog.getDialogPane().lookupButton(cancel);


        applyStyleRadioButton(radioButtonChoice1);
        applyStyleRadioButton(radioButtonChoice2);
        applyStyleRadioButton(radioButtonChoice3);
        applyStyleButton(applique);
        applyStyleButton(jouerButton);
        applyStyleButton(cancelButton);


        addListener(quixoChoiceController);
        Optional<ButtonType> result = dialog.showAndWait();

        return result;
    }

    public void initVBox() {
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));

        applyStylVBox(vBox);
    }

    public void showDefault() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, spacerVBox, applique);
    }

    public void showJcJ() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, add2TextField(), spacerVBox, applique);
    }

    public void showJcB() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, add1TextField(), lTypeBot, addBot(), spacerVBox, applique);
    }


    public void addListener(QuixoChoiceController controller) {
        applique.setOnAction(controller);

        groupChoice.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                controller.onGameModeChanged();
            }
        });
    }

    public HBox add1TextField() {
        textField1 = new TextField();
        textField1.setPromptText("Enter the player's name");
        applyStyleTextField(textField1);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        HBox hbox = new HBox(spacer1, textField1, spacer2);
        hbox.setPadding(new Insets(0, 10, 0, 10));
        hbox.setSpacing(10);

        return hbox;

    }

    public HBox add2TextField() {
        textField1 = new TextField();
        textField2 = new TextField();
        textField1.setPromptText("Enter Player 1's name");
        textField2.setPromptText("Enter Player 2's name");
        applyStyleTextField(textField1);
        applyStyleTextField(textField2);

        Region spacerLeft = new Region();
        Region spacerCenter = new Region();
        Region spacerRight = new Region();

        HBox.setHgrow(spacerLeft, Priority.ALWAYS);
        HBox.setHgrow(spacerCenter, Priority.ALWAYS);
        HBox.setHgrow(spacerRight, Priority.ALWAYS);

        HBox hbox = new HBox(spacerLeft, textField1, spacerCenter, textField2, spacerRight);
        hbox.setPadding(new Insets(0, 10, 0, 10));
        hbox.setSpacing(10);

        return hbox;
    }

    public HBox addBot(){
        groupBot = new ToggleGroup();
        radioButton1 = new RadioButton("Easy bot");
        radioButton2 = new RadioButton("Hard bot");
        radioButton1.setToggleGroup(groupBot);
        radioButton2.setToggleGroup(groupBot);
        radioButton1.setSelected(true);
        HBox hboxBot = new HBox(radioButton1, radioButton2);
        applyStyleRadioButton(radioButton1);
        applyStyleRadioButton(radioButton2);
        hboxBot.setSpacing(30);
        return hboxBot;
    }

    public TextField getTextField1() {
        return textField1;
    }

    public TextField getTextField2() {
        return textField2;
    }

    public ButtonType getButtonTypeJouer() {
        return ok;
    }

    public int getGameMode() {
        if (radioButtonChoice1.isSelected()) return 1;
        if (radioButtonChoice2.isSelected()) return 2;
        return 3;
    }

    public int getBotDifficulty() {
        if (radioButton1.isSelected()) return 1;
        if (radioButton2.isSelected()) return 2;
        return -1;
    }

    public Button getApplyButton() {
        return applique;
    }


    // Appliquer un style aux différents éléments

    public void applyStylVBox(VBox vBox) {
        vBox.setStyle(
                "-fx-background-color: #f5f0e1;" +
                        "-fx-border-color: #8b5e3c;" +
                        "-fx-border-width: 4;" +
                        "-fx-padding: 10;"
        );

    }

    public void applyStyleButton(Button button) {
        String normalStyle;
        String hoverStyle;

        if (button.getText().equals("Play")) {
            // Style vert pour Jouer
            normalStyle = "-fx-background-color: #4CAF50;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);";

            hoverStyle = "-fx-background-color: #45a049;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0, 0, 3);";

        } else if (button.getText().equals("Cancel")) {
            // Style rouge pour Annuler
            normalStyle = "-fx-background-color: #d9534f;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);";

            hoverStyle = "-fx-background-color: #c9302c;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0, 0, 3);";
        } else {
            // Style bois classique pour le reste
            normalStyle = "-fx-background-color: #8b5e3c;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);";

            hoverStyle = "-fx-background-color: #a47148;" +
                    "-fx-text-fill: white;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 14px;" +
                    "-fx-background-radius: 10;" +
                    "-fx-padding: 8 20 8 20;" +
                    "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0, 0, 3);";
        }

        button.setStyle(normalStyle);
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }


    public void applyStyleTextField(TextField textField) {
        textField.setStyle(
                "-fx-background-color: #f5f0e1;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #8b5e3c;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 6 10;" +
                        "-fx-font-size: 13px;" +
                        "-fx-text-fill: #3e2f1c;"
        );
        textField.setMinWidth(100);
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
    }

    public void applyStyleRadioButton(RadioButton radioButton) {
        radioButton.setStyle(
                "-fx-padding: 5 15 5 5;" +
                        "-fx-text-fill: #3e2f1c;" +
                        "-fx-background-color: transparent;" +
                        "-fx-cursor: hand;"
        );
    }

}
