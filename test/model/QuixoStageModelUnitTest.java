package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class QuixoStageModelUnitTest {

    private QuixoStageModel quixoStageModel;
    private QuixoBoard quixoBoard;
    private Model model;

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(Model.class);
        quixoStageModel = new QuixoStageModel("quixoStageModel", model);
        quixoBoard = Mockito.mock(QuixoBoard.class);

    }

    @Test
    public void testSetUpCallBacks() {
        quixoStageModel.setupCallbacks(quixoBoard, 1);


    }



    @Test
    public void testComputePartyResult() {
        quixoStageModel.computePartyResult(1);

        verify(model, times(1)).setIdWinner(0);

        quixoStageModel.computePartyResult(2);
        verify(model, times(1)).setIdWinner(1);


        verify(model, times(2)).stopStage();
    }

}
