package model;

import boardifier.model.GameElement;
import boardifier.model.GameStageModel;

public class CoupsElement extends GameElement {

    private int nbCoups; // in seconds

    public CoupsElement(GameStageModel gameStageModel) {
        super(gameStageModel);
        this.nbCoups = 0; // 3 minutes
    }

    public void incrementeCoups() {
        nbCoups++;
    }

    public int getNbCoups() {
        return nbCoups;
    }


}
