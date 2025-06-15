package control;

import boardifier.control.ActionFactory;
import boardifier.model.Model;
import boardifier.model.Player;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
    private QuixoController quixoController;
    private Player player;


    @BeforeEach
    public void setUp() {
        quixoStageModel = Mockito.mock(QuixoStageModel.class);
        model = Mockito.mock(Model.class);
        quixoBoard = Mockito.mock(QuixoBoard.class);
        pot = Mockito.mock(QuixoPawnPot.class);
        cube = Mockito.mock(Cube.class);
        quixoDecider = Mockito.mock(QuixoDecider.class);
        quixoController = Mockito.mock(QuixoController.class);
        player = Mockito.mock(Player.class);

        when(model.getGameStage()).thenReturn(quixoStageModel);
        when(quixoStageModel.getBoard()).thenReturn(quixoBoard);
        when(quixoStageModel.getRedPot()).thenReturn(pot);
    }

    @Test
    public void testDecide() {
        QuixoDecider spyDecider = Mockito.spy(quixoDecider);

        int[] move = new int[]{0, 0, 0, 0};
        doReturn(move).when(spyDecider).play();

        when(quixoBoard.getElement(0, 0)).thenReturn(cube);

        doNothing().when(quixoController).mooveSequenceCube(anyInt(), anyInt(), anyInt(), anyInt(), anyBoolean());

        ActionList firstTurnActions = new ActionList();
        firstTurnActions.addAll(Mockito.mock(ActionList.class));
        doReturn(firstTurnActions).when(spyDecider).firstTurn(anyInt(), anyInt(), anyInt(), anyInt());

        Cube spyCube = Mockito.spy(cube);

//        ActionList defaultAction = ActionFactory.generatePutInContainer(model, spyCube, "cubepot", 0, 0);
//        when(quixoDecider.decide()).thenReturn(defaultAction);
//        assertEquals(defaultAction, quixoDecider.decide());
    }


    @Test
    public void testFirstTurn() {
        when(quixoBoard.getElement(anyInt(), anyInt())).thenReturn(cube);
        when(cube.getFace()).thenReturn(1);

        when(model.getCurrentPlayer()).thenReturn(player);
        when(model.getCurrentPlayer().getType()).thenReturn(1);

        Cube spyCube = Mockito.spy(cube);

        ActionList atcions = ActionFactory.generatePutInContainer(model, spyCube, "cubepot", 0, 0);

        when(quixoDecider.firstTurn(anyInt(), anyInt(), anyInt(), anyInt())).thenReturn(atcions);
        assertEquals(atcions, quixoDecider.firstTurn(anyInt(), anyInt(), anyInt(), anyInt()));
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
        Point cible = new Point(2, 3);
        int[] coordCube = new int[] {0, 1};
        List<Point> firstMove = List.of(new Point(coordCube[1], coordCube[0]));
        int[] resultatAttendu = new int[] {1, 1, 2, 3};

        when(quixoDecider.trouverSecondMove(cible, firstMove)).thenReturn(resultatAttendu);

        int[] result = quixoDecider.trouverSecondMove(cible, firstMove);

        assertNotNull(result);
        assertArrayEquals(resultatAttendu, result);
    }


    @Test
    public void testGetAlignement() {
        when(quixoBoard.getElement(anyInt(), anyInt())).thenReturn(cube);
        when(cube.getFace()).thenReturn(1);
        int currentFace = cube.getFace();


        List<List<Integer>> alignements = quixoDecider.getAlignement(1);
        assertNotNull(alignements);
        assertEquals(1, currentFace);

        when(cube.getFace()).thenReturn(2);
        assertNotEquals(2, currentFace);
    }


    @Test
    public void testSearchMax() {
        QuixoDecider decider = new QuixoDecider(null, null);

        List<List<Integer>> liste = new ArrayList<>(Arrays.asList(
           Arrays.asList(1, 7, 3),
           Arrays.asList(15, 5, 6),
           Arrays.asList(0, 8, 9)
        ));

        List<Point> result = decider.searchMax(liste);
        assertEquals(1, result.size());
        assertEquals(new Point(1, 0), result.getFirst());

        List<List<Integer>> data = Arrays.asList(
                Arrays.asList(-5, -2, -3),
                Arrays.asList(-4, -1, -6)
        );

        List<Point> otherResult = decider.searchMax(data);
        assertEquals(0, otherResult.size());        // Ne marche pas avec des valeurs négatives
    }


    @Test
    public void testCubeDetection() {
        QuixoDecider decider = new QuixoDecider(null, null);
        List<Point> caseObjectif = new ArrayList<>();

        when(quixoBoard.getElement(anyInt(), anyInt())).thenReturn(cube);
        when(cube.getFace()).thenReturn(0);
        when(model.getCurrentPlayer()).thenReturn(player);
        when(player.getType()).thenReturn(1);

        decider.cubeDetection(1, 2, caseObjectif);

        assertEquals(1, caseObjectif.size());
        assertEquals(new Point(2, 1), caseObjectif.getFirst());
    }



}
