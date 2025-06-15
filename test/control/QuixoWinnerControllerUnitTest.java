package control;

import boardifier.control.Controller;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.configuration.IMockitoConfiguration;
import view.DialogView;
import view.QuixoView;
import view.WinnerScreen;


import static org.mockito.Mockito.*;

public class QuixoWinnerControllerUnitTest {

    private QuixoView quixoView;
    private QuixoWinnerController quixoWinnerController;
    private ButtonType button, recommencer;
    private Controller controller;
    private WinnerScreen winnerScreen;
    private System system;

    @BeforeAll
    public static void initToolkit() {
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        quixoView = mock(QuixoView.class);
        controller = mock(Controller.class);
        winnerScreen = mock(WinnerScreen.class);
        button = new ButtonType("");
        recommencer = new ButtonType("");
        system = mock(System.class);


        quixoWinnerController = new QuixoWinnerController(null, winnerScreen, controller, quixoView);

        when(winnerScreen.getQuitter()).thenReturn(button);
        when(winnerScreen.getRecommencer()).thenReturn(recommencer);

    }

//    @Test
//    public void TestHandleButtonIf() {
//        quixoWinnerController.handleButton(button);
//
//        verify(system).exit(0);
//
//    }

    @Test
    public void TestHandleButtonElse() {
        quixoWinnerController.handleButton(recommencer);

        verify(controller).stopGame();
        verify(quixoView).resetView();
    }

}
