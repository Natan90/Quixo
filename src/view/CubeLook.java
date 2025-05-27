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

        // Crée un rectangle pour représenter le carré
        Rectangle square = new Rectangle(50, 50); // Taille 50x50 (modifiable)
        square.setStroke(Color.BLACK); // Bordure noire

        // Détermine la couleur de fond en fonction de l'état du cube
        if (cube.isJouable()) {
            square.setFill(Color.LIGHTGREEN); // Fond vert clair si jouable
        } else {
            square.setFill(Color.LIGHTGRAY); // Fond gris clair si non jouable
        }

        // Crée un texte pour afficher "X" ou "O"
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

        // Superpose le texte sur le rectangle
        StackPane stack = new StackPane();
        stack.getChildren().addAll(square, text);

        // Ajoutez le stack à votre scène ou conteneur principal
        // Exemple : root.getChildren().add(stack);

    }

}
