package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;


public class Cube extends GameElement {
    public static int CUBE_WHITE = 0;

    private int face;
    private int color;

    public Cube(int color, GameStageModel gameStageModel) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("cube",69);
        type = ElementTypes.getType("cube");
        this.face = face;
        this.color = color;
    }

    public int getColor() {
        return color;
    }
    public int getFace(){
        return face;
    }

}
