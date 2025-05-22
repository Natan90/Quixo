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
        coordCube = new int[]{2, 0};
        model = Mockito.mock(Model.class);
    }

    @Test
    public void testComputeValidCells() {
        Cube cube = Mockito.mock(Cube.class);
        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 && j == 0) {
                    // Quand on est en (0,0), on met la face du cube à celle de l'adversaire
                    when(cube.getFace()).thenReturn(2);
                } else {
                    // Sinon, on les mets en cube blanc
                    when(cube.getFace()).thenReturn(0);
                }
                Assertions.assertNotNull(cube);
                board.addElement(cube, i, j);
            }
        }
        // Vérification si on prend la face de l'adversaire
        when(cube.getFace()).thenReturn(2);
        when(model.getIdPlayer()).thenReturn(0);
        Assertions.assertNotEquals(cube.getFace(), model.getIdPlayer());
        when(cube.getFace()).thenReturn(1);
        when(model.getIdPlayer()).thenReturn(1);
        Assertions.assertEquals(cube.getFace(), model.getIdPlayer());

        List<Point> valid = board.computeValidCells(true, null, model);
        Assertions.assertEquals(valid, board.computeValidCells(true, null, model));

        // Vérifier si les cases sont bien dans les cases valides
        Assertions.assertFalse(valid.contains(new Point(0, 0)));

        List<Point> secondMove = board.computeValidCells(false, coordCube, model);
        Assertions.assertFalse(secondMove.contains(new Point(0, 2)));   // Même position
        Assertions.assertFalse(secondMove.contains(new Point(1, 2)));    // Même colonne
        Assertions.assertFalse(secondMove.contains(new Point(0, 1)));    // Même ligne

        Assertions.assertFalse(secondMove.contains(new Point(2, 3)));   // Mauvaise saisie
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
}
