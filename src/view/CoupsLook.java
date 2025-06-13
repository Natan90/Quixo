package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.CoupsElement;

public class CoupsLook extends ElementLook {

    protected int fontSize;
    protected String color;
    protected CoupsElement coupsElement;
    protected int coups;
    protected Text text;
    protected String nbCoups;

    public CoupsLook(int fontSize, String color, GameElement element) {
        super(element);
        this.fontSize = fontSize;
        this.color = color;

        nbCoups = "Number of moves : ";
    }

    @Override
    public void render() {
        clearShapes();

        coupsElement = (CoupsElement) element;
        coups = coupsElement.getNbCoups();

        text = new Text(nbCoups + coups);
        text.setFont(new Font(fontSize));
        text.setFill(Color.web(color));
        text.setLayoutX(15);
        text.setLayoutY(150);

        addShape(text);
    }

    public void update() {
        String newCoups = String.valueOf(coupsElement.getNbCoups());
        text.setText(nbCoups + newCoups);
    }

    public void incrementeCoups() {
        coupsElement.incrementeCoups();
        String newCoups = String.valueOf(coupsElement.getNbCoups());
        text.setText(nbCoups + newCoups);
    }


}
