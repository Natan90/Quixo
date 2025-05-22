package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class QuixoStageModelUnitTest {

    private QuixoStageModel quixoStageModel;
    private QuixoBoard quixoBoard;
    private Model model;

    @BeforeEach
    public void setUp() {
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        quixoBoard = Mockito.mock(QuixoBoard.class);
        model = Mockito.mock(Model.class);
    }


    @Test
    public void testComputePartyResult() {
        quixoStageModel.computePartyResult(1);

        // Pas finit
        verify(model, times(1)).setIdWinner(0);
        verify(model, times(1)).stopStage();
    }

}
