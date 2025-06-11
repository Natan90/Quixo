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


    private QuixoStageModel quixoStageModel;
    private Model model;


    public DialogView(Model model) {
        this.model = model;
//        super(model, stage, rootPane);
    }

    public Optional<ButtonType> initDialog() {
        dialog = new Dialog<>();

        cancel = new ButtonType("Cancel");
        ok = new ButtonType("Jouer");

        lTypeJeu = new Label("Choisissez votre type de jeu : ");
        lTypeBot = new Label("Choisissez la difficulté du bot : ");

        groupChoice = new ToggleGroup();
        radioButtonChoice1 = new RadioButton("Joueur vs Joueur");
        radioButtonChoice2 = new RadioButton("Joueur vs Bot");
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

        applique = new Button("Appliquer les choix");


        hbox = new HBox();
        hbox2 = new HBox();

        hbox2.getChildren().addAll(radioButtonChoice1, radioButtonChoice2, radioButtonChoice3);
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


        addListener();
        Optional<ButtonType> result = dialog.showAndWait();

        return result;
    }

    public void initVBox() {
        vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 10, 10, 10));
    }

    public void showDefault() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, applique);
    }

    public void showJcJ() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, add2TextField(), applique);
    }

    public void showJcB() {
        if (!vBox.getChildren().isEmpty())
            vBox.getChildren().clear();
        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, add1TextField(), lTypeBot, addBot(), applique);
    }

//    public void showBcB() {
//        if (!vBox.getChildren().isEmpty())
//            vBox.getChildren().clear();
//        vBox.getChildren().addAll(lTypeJeu, hbox2, hbox, separtor, applique);
//    }


    public void addListener() {
        QuixoChoiceController controller = new QuixoChoiceController(model, this);
        applique.setOnAction(controller);
    }

    public HBox add1TextField() {
        textField1 = new TextField();
        textField1.setPromptText("Entrer le nom du joueur 1");
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
        textField1.setPromptText("Entrer le nom du joueur 1");
        textField2.setPromptText("Entrer le nom du joueur 2");
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
        radioButton1 = new RadioButton("Bot facile");
        radioButton2 = new RadioButton("Bot difficile");
        radioButton1.setToggleGroup(groupBot);
        radioButton2.setToggleGroup(groupBot);
        radioButton1.setSelected(true);
        HBox hboxBot = new HBox(radioButton1, radioButton2);
        applyStyleRadioButton(radioButton1);
        applyStyleRadioButton(radioButton2);
        hboxBot.setSpacing(30);
        return hboxBot;
    }

    public RadioButton getRadioButtonChoice1() {
        return radioButtonChoice1;
    }

    public RadioButton getRadioButtonChoice2() {
        return radioButtonChoice2;
    }


    public RadioButton getRadioButtonBot1() {
        return radioButton1;
    }

    public RadioButton getRadioButtonBot2() {
        return radioButton2;
    }

    public ButtonType getButtonTypeJouer() {
        return ok;
    }

    public Dialog getDialog() {
        return dialog;
    }


    // Appliquer un style aux différents éléments
    public void applyStyleButton(Button button) {
        // Style par défaut
        String normalStyle = "-fx-background-color: linear-gradient(to right, #4facfe, #00f2fe);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 5, 0, 0, 2);";

        // Style au survol
        String hoverStyle = "-fx-background-color: linear-gradient(to right, #00f2fe, #4facfe);" +
                "-fx-text-fill: white;" +
                "-fx-font-weight: bold;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 20;" +
                "-fx-padding: 8 20 8 20;" +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.4), 6, 0, 0, 3);";

        button.setStyle(normalStyle);

        // Quand la souris survol
        button.setOnMouseEntered(e -> button.setStyle(hoverStyle));
        // Quand elle ne survol plus
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    public void applyStyleTextField(TextField textField) {
        textField.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #4facfe;" +
                        "-fx-border-radius: 10;" +
                        "-fx-padding: 6 10;" +
                        "-fx-font-size: 13px;"
        );
        textField.setMinWidth(100);
        textField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(textField, Priority.ALWAYS);
    }

    public void applyStyleRadioButton(RadioButton radioButton) {
        radioButton.setStyle("-fx-padding: 5 15 5 5;" +
                "-fx-background-color: transparent;" +
                "-fx-cursor: hand;");
    }

}
