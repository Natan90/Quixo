package model;

import boardifier.model.ElementTypes;
import boardifier.model.GameElement;
import boardifier.model.GameStageModel;


public class Cube extends GameElement {
    public static int CUBE_WHITE = 0;

    private int face;
    private int color;
    private boolean isJouable;
    public boolean selected;

    public Cube(int color, GameStageModel gameStageModel, boolean isJouable, boolean selected) {
        super(gameStageModel);
        // registering element types defined especially for this game
        ElementTypes.register("cube",69);
        type = ElementTypes.getType("cube");
        this.face = 0; // par defaut, la face est blanche, et l'etat par défaut de blanche est 0
        this.color = color;
        this.isJouable = isJouable;
        this.selected = selected;
    }

    public int getColor() {
        return color;
    }
    public int getFace(){
        return face;
    }
    public void setFace(int face){
        this.face = face;
    }
    public void setJouable(){
        isJouable = true;
    }
    public void resetJouable() {
        isJouable = false;
    }
    public boolean isJouable() {
        return isJouable;
    }
    public void setSelected() {
        selected = true;
    }


}
