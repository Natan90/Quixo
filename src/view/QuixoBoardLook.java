package view;

import boardifier.model.ContainerElement;
import boardifier.view.ClassicBoardLook;
import javafx.scene.paint.Color;

public class QuixoBoardLook extends ClassicBoardLook {

    public QuixoBoardLook(int size, ContainerElement element) {
        // NB: To have more liberty in the design, GridLook does not compute the cell size from the dimension of the element parameter.
        // If we create the 3x3 board by adding a border of 10 pixels, with cells occupying all the available surface,
        // then, cells have a size of (size-20)/3
        super(size / 3, element, -1, Color.WHITE, Color.LIGHTGREY, 4, Color.BLACK, 3, Color.BLACK, true);
    }
}
