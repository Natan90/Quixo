package view;

import boardifier.model.GameStageModel;
import boardifier.view.*;
import model.CoupsElement;
import model.QuixoStageModel;
import model.TimerElement;

public class QuixoStageView extends GameStageView {

    public RedPawnPotLook redPawnPotLook;
    private TimerLook timerLook;
    private CoupsLook coupsLook;

    public QuixoStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    public void setTimerElement(TimerElement timerElement) {
        timerLook = new TimerLook(24, "#000000", timerElement);
        addLook(timerLook);
    }

    public void setCoupsElement(CoupsElement coupsElement) {
        coupsLook = new CoupsLook(15, "#000000", coupsElement);
        addLook(coupsLook);
    }

    @Override
    public void createLooks() {
        QuixoStageModel model = (QuixoStageModel)gameStageModel;

        addLook(new QuixoBoardLook(300, model.getBoard()));
        redPawnPotLook = new RedPawnPotLook(320, 80, model.getRedPot());
        addLook(redPawnPotLook);

        for(int i=0;i<25;i++) {
            addLook(new CubeLook(model.getCubes()[i]));
        }

        addLook(new TextLook(24, "0x000000", model.getPlayerName()));

        setTimerElement(((QuixoStageModel)gameStageModel).getTimer());
        setCoupsElement((((QuixoStageModel) gameStageModel).getCoups()));

        /* Example to show how to set a global container to layout all looks in the root pane
           Must also uncomment lines in HoleStageFactory and HoleStageModel

         */

    }

    public TimerLook getTimerLook() {
        return timerLook;
    }

    public CoupsLook getCoupsLook() {
        return coupsLook;
    }
}
