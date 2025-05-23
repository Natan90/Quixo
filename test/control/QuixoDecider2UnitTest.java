package control;

import boardifier.control.ActionFactory;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

public class QuixoDecider2UnitTest {

    private QuixoDecider2 quixoDecider2;
    private QuixoStageModel quixoStageModel;
    private GameStageModel gameStageModel;
    private Model model;
    private QuixoBoard board;
    private int[][] tabAfterMove;
    private Cube cube;


    @BeforeEach
    public void setup() {
        model = Mockito.mock(Model.class);
        quixoDecider2 = Mockito.mock(QuixoDecider2.class);
        gameStageModel = Mockito.mock(GameStageModel.class);
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        board = new QuixoBoard(0, 0, gameStageModel);
        tabAfterMove = new int[5][5];
        cube = Mockito.mock(Cube.class);
    }

    @Test
    public void TestDecide() {
        when(model.getGameStage()).thenReturn(gameStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);
        when(quixoDecider2.getScore(2, tabAfterMove)).thenReturn(0);
        when(quixoDecider2.getScore(1, tabAfterMove) * 2).thenReturn(0);

        ActionList testDecide = ActionFactory.generatePutInContainer(model, cube, "quixoboard", 0,0);

        quixoDecider2.decide();
        assertNotEquals(quixoDecider2.decide(), testDecide);
        // Faire un assertEquals
    }


    @Test
    public void TestgetScore() {

    }

}
