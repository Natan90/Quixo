package control;

import boardifier.control.ActionFactory;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doReturn;
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
                {1, 0, 2, 0, 1},
                {0, 2, 0, 2, 0},
                {2, 0, 2, 0, 0},
                {2, 0, 0, 0, 0},
                {1, 0, 0, 0, 1}};


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

    }


    @Test
    public void TestGetPointsForAlignement() {
        QuixoDecider2 otherQuiwoDecider = new QuixoDecider2(model, controller);

        assertEquals(50, otherQuiwoDecider.getPointsForAlignment(3));
        assertEquals(1000, otherQuiwoDecider.getPointsForAlignment(4));
        assertEquals(10000, otherQuiwoDecider.getPointsForAlignment(5));
    }

    @Test
    public void TestMoveSequenceCube() {
        when(quixoDecider2.moveSequenceCube(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(tabBoardAfterMove);


        int[] firstRowExcepted = {1, 0, 2, 0, 1};
        assertArrayEquals(firstRowExcepted, quixoDecider2.moveSequenceCube(0,4,0,2)[0]);

        int[] secondRowExcepted = {1, 0, 0, 0, 1};
        assertArrayEquals(secondRowExcepted, quixoDecider2.moveSequenceCube(0,0,0,3)[4]);

        int firstColExcepted = 1;
        assertEquals(firstColExcepted, quixoDecider2.moveSequenceCube(0,4,2,4)[0][4]);

        int secondColExcepted = 1;
        assertEquals(secondColExcepted, quixoDecider2.moveSequenceCube(4,4,2,4)[4][4]);
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
