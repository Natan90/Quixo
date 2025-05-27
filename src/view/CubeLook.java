package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Cube;

/**
 * The look of the Pawn is fixed, with a single characters representing the value of the pawn
 * and a black or red background.
 */
public class CubeLook extends ElementLook {

    public CubeLook(GameElement element) {
        super(element);
    }

    public void render() {
        Cube cube = (Cube) element;

        Rectangle square = new Rectangle(50, 50);
        square.setStroke(Color.BLACK); // Bordure noire

        // Détermine la couleur de fond en fonction de l'état du cube
        if (cube.isJouable()) {
            square.setFill(Color.LIGHTGREEN);
        } else {
            square.setFill(Color.LIGHTGRAY);
        }

        Text text = new Text();
        if (cube.getFace() == 1) {
            text.setText("X");
            text.setFill(Color.BLACK); // Couleur du texte
        } else if (cube.getFace() == 2) {
            text.setText("O");
            text.setFill(Color.BLACK);
        } else {
            text.setText(""); // Pas de texte si la face est 0
        }


        StackPane stack = new StackPane();
        stack.getChildren().addAll(square, text);

        // Ajoutez le stack à votre scène ou conteneur principal
        // Exemple : root.getChildren().add(stack);

    }

}
