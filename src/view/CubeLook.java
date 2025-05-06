package view;

import boardifier.model.GameElement;
import boardifier.view.ConsoleColor;
import boardifier.view.ElementLook;
import model.Cube;

/**
 * The look of the Pawn is fixed, with a single characters representing the value of the pawn
 * and a black or red background.
 */
public class CubeLook extends ElementLook {

    public CubeLook(GameElement element) {
        super(element, 1, 1);
    }

    protected void render() {

        Cube cube = (Cube)element;

        if (cube.getFace() == 0)
            shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + " " + ConsoleColor.RESET;
        else if (cube.getFace() == 1)
            shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + "X" + ConsoleColor.RESET;
        else if (cube.getFace() == 2)
            shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + "O" + ConsoleColor.RESET;

    }
}
