package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.StageElementsFactory;
import boardifier.model.TextElement;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class QuixoStageFactoryUnitTest {

    private QuixoStageModel quixoStageModel;
    private QuixoStageFactory quixoStageFactory;

    @BeforeEach
    public void setUp() {
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        quixoStageFactory = new QuixoStageFactory(quixoStageModel);
        when(quixoStageModel.getCurrentPlayerName()).thenReturn("player1");
    }

    @Test
    public void TestSetup() {
        quixoStageFactory.setup();

        verify(quixoStageModel, times(1)).setPlayerName(any(TextElement.class));
        verify(quixoStageModel, times(1)).setBoard(any(QuixoBoard.class));
        verify(quixoStageModel, times(1)).setCubes(argThat(Cube -> Cube != null && Cube.length == 25));
    }

}
