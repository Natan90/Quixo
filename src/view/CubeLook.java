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

    public void render() {
        Cube cube = (Cube)element;
        System.out.println("render() appelé pour " + cube + " jouable=" + cube.isJouable());

        if (cube.isJouable()) {
            System.out.println("dans isJouable");
            if (cube.getFace() == 0)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.GREEN_BACKGROUND + " " + ConsoleColor.RESET;
            else if (cube.getFace() == 1)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.GREEN_BACKGROUND + "X" + ConsoleColor.RESET;
            else if (cube.getFace() == 2)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.GREEN_BACKGROUND + "O" + ConsoleColor.RESET;
        } else {
            System.out.println("dans else");
            if (cube.getFace() == 0)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + " " + ConsoleColor.RESET;
            else if (cube.getFace() == 1)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + "X" + ConsoleColor.RESET;
            else if (cube.getFace() == 2)
                shape[0][0] = ConsoleColor.BLACK + ConsoleColor.WHITE_BACKGROUND + "O" + ConsoleColor.RESET;
        }


    }

}
