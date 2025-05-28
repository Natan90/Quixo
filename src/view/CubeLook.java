package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.geometry.Bounds;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Cube;

/**
 * The look of the Pawn is fixed, with a single characters representing the value of the pawn
 * and a black or red background.
 */
public class CubeLook extends ElementLook {

    private Rectangle rectangle;
    private int radius;

    public CubeLook(GameElement element) {
        super(element);
    }

    public void render() {
        Cube cube = (Cube)element;
        rectangle = new Rectangle(-45, -45, 95, 95);
        if (cube.getColor() == Cube.CUBE_WHITE) {
            rectangle.setFill(Color.BLACK);
        }
        else {
            rectangle.setFill(Color.RED);
        }

        addShape(rectangle);
        // NB: text won't change so no need to put it as an attribute
        Text text = new Text(String.valueOf(cube.getFace()));
        text.setFont(new Font(24));
        if (cube.getColor() == Cube.CUBE_WHITE) {
            text.setFill(Color.valueOf("0xFFFFFF"));
        }
        else {
            text.setFill(Color.valueOf("0x000000"));
        }
        Bounds bt = text.getBoundsInLocal();
        text.setX(-bt.getWidth()/2);
        // since numbers are always above the baseline, relocate just using the part above baseline
        text.setY(text.getBaselineOffset()/2-4);
        addShape(text);

    }

}
