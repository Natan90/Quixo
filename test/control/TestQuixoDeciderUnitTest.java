package control;

import boardifier.control.Controller;
import boardifier.model.GameStageModel;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.view.View;
import model.Cube;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.naming.ldap.Control;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class TestQuixoDeciderUnitTest {

    private QuixoStageModel quixoStageModel;
    private Model model;
    private QuixoBoard quixoBoard;
    private QuixoPawnPot pot;
    private Cube cube;
    private QuixoDecider quixoDecider;


    @BeforeEach
    public void setUp() {
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        model = Mockito.mock(Model.class);
        quixoBoard = Mockito.mock(QuixoBoard.class);
        pot = Mockito.mock(QuixoPawnPot.class);
        cube = Mockito.mock(Cube.class);
        quixoDecider = Mockito.mock(QuixoDecider.class);

        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(quixoBoard);
        when(quixoStageModel.getRedPot()).thenReturn(pot);
    }

    @Test
    public void testDecide() {

        // À continuer
    }


    @Test
    public void testFirstTurn() {
        when(quixoBoard.getElement(anyInt(), anyInt())).thenReturn(cube);


        ActionList testFirstTurn = quixoDecider.firstTurn(0, 0, 4, 0);
        assertTrue(testFirstTurn.getActions().getFirst().contains("PutInContainer"));
        // À terminer
    }


    @Test
    public void testPlay() {
        QuixoDecider spyDecider = Mockito.spy(quixoDecider);

        List<List<Integer>> fakeAlignementsOpponent = Arrays.asList(
                Arrays.asList(0, 3, 0, 0, 0),
                Arrays.asList(0, 0, 0, 0, 0),
                Arrays.asList(0, 0)
        );
        doReturn(fakeAlignementsOpponent).when(spyDecider).getAlignement(1);

        doCallRealMethod().when(spyDecider).getAlignement(2);
    }


    @Test
    public void testTrouverSecondMove() {

    }


    @Test
    public void testGetAlignement() {
        List<List<Integer>> alignements = quixoDecider.getAlignement(1);
        List<Integer> lignes = alignements.get(0);
        List<Integer> colonnes = alignements.get(1);
        List<Integer> diagonales = alignements.get(2);
        

    }


    @Test
    public void testSearchMax() {
        List<List<Integer>> liste = new ArrayList<>(Arrays.asList(
           Arrays.asList(1, 7, 3),
           Arrays.asList(15, 5, 6),
           Arrays.asList(0, 8, 9)
        ));

        List<Point> coordMax = List.of(
                new Point(1, 0)
        );
        assertTrue(quixoDecider.searchMax(liste).contains(coordMax.getFirst()));
    }


    @Test
    public void testCubeDetection() {

        List<Point> caseObjectif = Mockito.mock(List.class);

        when(quixoBoard.getElement(anyInt(), anyInt())).thenReturn(cube);
        when(cube.getFace()).thenReturn(0);

        quixoDecider.cubeDetection(1, 2, caseObjectif);

        verify(caseObjectif, atLeastOnce()).add(any(Point.class));
    }



}
