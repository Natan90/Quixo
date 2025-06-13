package view;

import boardifier.model.GameElement;
import boardifier.view.ElementLook;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.TimerElement;

public class TimerLook extends ElementLook {

    protected int fontSize;
    protected String color;
    protected TimerElement timer;
    protected String currentTime;
    protected Text text;

    public TimerLook(int fontSize, String color, GameElement element) {
        super(element);
        this.fontSize = fontSize;
        this.color = color;
    }

    @Override
    public void render() {
        clearShapes();

        timer = (TimerElement) element;
        currentTime = timer.getFormattedTime();

        text = new Text(currentTime);
        text.setFont(new Font(fontSize));
        text.setFill(Color.web(color));
        text.setLayoutX(50);
        text.setLayoutY(100);

        addShape(text);
    }

    public void update() {
        String newTime = timer.getFormattedTime();
        if (!newTime.equals(currentTime)) {
            currentTime = newTime;
            text.setText(currentTime);
        }
    }

    public void increment3(int compteurTour) {
        timer.increment3();
        String newTime = timer.getFormattedTime();
        if (!newTime.equals(currentTime)) {
            currentTime = newTime;
            text.setText(currentTime);
        }
    }

    public void setTimerText(String time) {
        text.setText(time);
    }



}
