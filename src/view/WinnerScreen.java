package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class WinnerScreen {

    public void display(String winnerName) {
        Stage window = new Stage();
        window.setTitle("Winner!");
        window.setMinWidth(250);

        Label label = new Label("Winner: " + winnerName);

        Button returnButton = new Button("Return to Menu");
        returnButton.setOnAction(e -> {
            // Code pour retourner au menu principal
            // Exemple : Fermer cette fenêtre et afficher le menu principal
            window.close();
            // Code pour afficher ou revenir à l'écran du menu principal
            // Exemple : new MainMenu().start(new Stage());
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, returnButton);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}