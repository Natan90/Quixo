package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuixoBoardUnitTest {


    private GameStageModel gameStageModel;
    private QuixoStageModel quixoStageModel;
    private QuixoBoard board;
    private int[] coordCube;
    private Model model;


    @BeforeEach
    public void setUp() {
        gameStageModel = Mockito.mock(GameStageModel.class);
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        board = new QuixoBoard(0, 0, gameStageModel);
        coordCube = new int[]{0, 1, 2};
        model = Mockito.mock(Model.class);
    }

    @Test
    public void testComputeValidCellsFirstMove() {
        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);
        when(model.getIdPlayer()).thenReturn(0);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cube cube = Mockito.mock(Cube.class);
                if (i == 0 && j == 0) {
                    // Quand on est en (0,0), on met la face du cube à celle de l'adversaire
                    when(cube.getFace()).thenReturn(2);
                } else {
                    // Sinon, on les mets en cube blanc
                    when(cube.getFace()).thenReturn(0);
                }
                board.addElement(cube, i, j);
            }
        }
        List<Point> valid = board.computeValidCells(true, null, model);
        Assertions.assertEquals(valid, board.computeValidCells(true, coordCube, model));

        // Vrifier si les cases sont bien dans les cases valides
        Assertions.assertFalse(valid.contains(new Point(0, 0)));
        Assertions.assertTrue(valid.contains(new Point(0, 1)));
    }

    @Test
    public void testComputeValidCellsCentralCells() {
        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);
        when(model.getIdPlayer()).thenReturn(0);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cube cube = Mockito.mock(Cube.class);
                when(cube.getFace()).thenReturn(0);
                board.addElement(cube, i, j);
            }
        }
        List<Point> valid = board.computeValidCells(true, null, model);

        // Vérification des cases centrales
        int[] centralCells = new int[]{1, 2, 3};
        for(int i = 0; i < centralCells.length; i++) {
            for(int j = 0; j < centralCells.length; j++) {
                Assertions.assertFalse(valid.contains(new Point(centralCells[i], centralCells[j])));
            }
        }
    }

    @Test
    public void testSetValidCells() {
        board.setValidCells(true, coordCube, model);

    }

}
