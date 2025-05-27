package view;

import boardifier.model.GameStageModel;
import boardifier.view.*;
import model.QuixoStageModel;

public class QuixoStageView extends GameStageView {
    public QuixoStageView(String name, GameStageModel gameStageModel) {
        super(name, gameStageModel);
    }

    @Override
    public void createLooks() {
        QuixoStageModel model = (QuixoStageModel)gameStageModel;

        addLook(new QuixoBoardLook(300, model.getBoard()));
        addLook(new RedPawnPotLook(320, 80,model.getRedPot()));

        for(int i=0;i<25;i++) {
            addLook(new CubeLook(model.getCubes()[i]));
        }

        addLook(new TextLook(24, "0x000000", model.getPlayerName()));

        /* Example to show how to set a global container to layout all looks in the root pane
           Must also uncomment lines in HoleStageFactory and HoleStageModel
        ContainerLook mainLook = new ContainerLook(model.getRootContainer(), -1);
        mainLook.setPadding(10);
        mainLook.setVerticalAlignment(ContainerLook.ALIGN_MIDDLE);
        mainLook.setHorizontalAlignment(ContainerLook.ALIGN_CENTER);
        addLook(mainLook);

         */
    }
}
