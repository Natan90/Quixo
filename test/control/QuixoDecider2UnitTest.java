package control;

import boardifier.model.GameStageModel;
import model.QuixoBoard;
import model.QuixoStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class QuixoDecider2UnitTest {

    private QuixoDecider2 quixoDecider2;
    private QuixoStageModel quixoStageModel;
    private GameStageModel gameStageModel;
    private QuixoBoard board;

    @BeforeEach
    public void setup() {
        gameStageModel = Mockito.mock(GameStageModel.class);
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        board = new QuixoBoard(0, 0, gameStageModel);
    }

    @Test
    public void TestDecide() {

    }

}
