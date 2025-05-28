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

    public CubeLook(GameElement element) {
        super(element);
    }

    public void render() {
        Cube cube = (Cube)element;
        rectangle = new Rectangle(-45, -45, 95, 95);
        rectangle.setFill(Color.BLACK);

        addShape(rectangle);
        // NB: text won't change so no need to put it as an attribute
        Text text = null;
        if (cube.getFace() == 0){
            text = new Text("");
        } else if (cube.getFace() == 1) {
            text = new Text("X");
        }else if (cube.getFace() == 2){
            text = new Text("O");
        }
        text.setFont(new Font(24));
        text.setFill(Color.valueOf("0xFFFFFF"));
        Bounds bt = text.getBoundsInLocal();
        text.setX(-bt.getWidth()/2);
        // since numbers are always above the baseline, relocate just using the part above baseline
        text.setY(text.getBaselineOffset()/2-4);
        addShape(text);

    }

}
