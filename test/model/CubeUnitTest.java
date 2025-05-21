package model;

import boardifier.model.GameStageModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CubeUnitTest {

    private GameStageModel model;
    private Cube cube;

    @BeforeEach
    public void setUp() {
        model = Mockito.mock(GameStageModel.class);
        cube = new Cube(0, model, true);
    }
    @Test
    public void testSetFace() {
        cube.setFace(3);
        Assertions.assertEquals(3, cube.getFace());
    }

    @Test
    public void setJouable() {
        cube.setJouable();
        Assertions.assertTrue(cube.isJouable());
    }

    @Test
    public void testResetJouable() {
        cube.resetJouable();
        Assertions.assertFalse(cube.isJouable());
    }


}
