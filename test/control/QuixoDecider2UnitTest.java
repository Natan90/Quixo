package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
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

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public class QuixoDecider2UnitTest {

    private QuixoDecider2 quixoDecider2;
    private QuixoStageModel quixoStageModel;
    private GameStageModel gameStageModel;
    private Model model;
    private QuixoBoard board;
    private int[][] tabAfterMove;
    int[][] tabBoardAfterMove;
    private Cube cube;
    private QuixoController controller;


    @BeforeEach
    public void setup() {
        controller = Mockito.mock(QuixoController.class);
        model = Mockito.mock(Model.class);
        quixoDecider2 = Mockito.mock(QuixoDecider2.class);
        gameStageModel = Mockito.mock(GameStageModel.class);
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        board = Mockito.mock(QuixoBoard.class);
        cube = Mockito.mock(Cube.class);

        tabAfterMove = new int[5][5];
        tabBoardAfterMove = new int[][]{
                {0, 0, 2, 0, 0},
                {0, 2, 0, 2, 0},
                {2, 0, 0, 0, 0},
                {1, 0, 0, 0, 0},
                {1, 0, 0, 0, 0}};


        when(board.getElement(anyInt(), anyInt())).thenReturn(cube);
    }

    @Test
    public void TestDecide() {
        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);
        when(quixoDecider2.getScore(2, tabAfterMove)).thenReturn(0);
        when(quixoDecider2.getScore(1, tabAfterMove) * 2).thenReturn(0);

        ActionList testDecide = ActionFactory.generatePutInContainer(model, cube, "quixoboard", 0,0);

        quixoDecider2.decide();
        assertNotEquals(quixoDecider2.decide(), testDecide);
        // Faire un assertEquals
    }


    @Test
    public void TestGetPointsForAlignement() {
        assertEquals(50, quixoDecider2.getPointsForAlignment(3));
        assertEquals(1000, quixoDecider2.getPointsForAlignment(4));
        assertEquals(10000, quixoDecider2.getPointsForAlignment(5));
    }

    @Test
    public void TestMoveSequenceCube() {
        controller.currentPlayer = 1;


        int[][] result = quixoDecider2.moveSequenceCube(4,0,2,0);

        assertTrue(Arrays.deepEquals(tabBoardAfterMove, result));
//        assertEquals(tabBoardAfterMove, quixoDecider2.moveSequenceCube(4, 0, 2, 0));
    }


    @Test
    public void TestGetScore() {
        quixoDecider2.getScore(2, tabBoardAfterMove);

        assertEquals(2, tabBoardAfterMove[2][0]);
        assertEquals(2, tabBoardAfterMove[2][2]);

        assertEquals(2, tabBoardAfterMove[1][1]);
        assertEquals(2, tabBoardAfterMove[1][3]);
    }

}
