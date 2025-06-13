package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class TimerElement extends GameElement {

    private int timeLeft; // in seconds

    public TimerElement(GameStageModel gameStageModel) {
        super(gameStageModel);
        this.timeLeft = 180; // 3 minutes
    }

    public void decrement() {
        if (timeLeft > 0) {
            timeLeft--;
            addChangeFaceEvent();
        }
    }

    public void increment3() {
        timeLeft++;
        addChangeFaceEvent();
    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
        addChangeFaceEvent();
    }

    public int getTimeLeft() {
        return timeLeft;
    }

    public String getFormattedTime() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
}
