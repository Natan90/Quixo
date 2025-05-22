package model;

import boardifier.model.GameStageModel;
import boardifier.model.Model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuixoBoardUnitTest {


    private GameStageModel gameStageModel;
    private QuixoStageModel quixoStageModel;
    private QuixoBoard board;
    private int[] coordCube;
    private Model model;
    private Cube cube;


    @BeforeEach
    public void setUp() {
        gameStageModel = Mockito.mock(GameStageModel.class);
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        board = new QuixoBoard(0, 0, gameStageModel);
        model = Mockito.mock(Model.class);
        cube = Mockito.mock(Cube.class);
    }

    @Test
    public void testComputeValidCells() {
        coordCube = new int[]{2, 0};
        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(board);
        when(model.getIdPlayer()).thenReturn(1); // joueur 1

        Cube validCube = mock(Cube.class);
        when(validCube.getFace()).thenReturn(0); // face neutre, autorisée

        board.addElement(validCube, 0, 1); // On met ce cube en (0,1)

        List<Point> valid = board.computeValidCells(true, coordCube, model);

        assertTrue(valid.contains(new Point(0, 1)));
        //Coos qui marche pas
        assertFalse(valid.contains(new Point(2, 2)));

        List<Point> valid2 = board.computeValidCells(false, coordCube, model);
        // Même point
        assertFalse(valid.contains(new Point(0, 2)));
        // N'a pas de ligne ou colonne correspondante
        assertFalse(valid.contains(new Point(3, 4)));
        // Mauvaise saisie (même colonne et ligne entre 1 inclus et 3 inclus)
        assertFalse(valid.contains(new Point(3, 2)));
    }
}
