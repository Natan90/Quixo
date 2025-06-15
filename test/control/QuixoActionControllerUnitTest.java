package control;

import boardifier.control.Controller;
import boardifier.model.Model;
import boardifier.view.View;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.DialogView;
import view.QuixoView;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class QuixoActionControllerUnitTest {

    private QuixoView quixoView;
    private QuixoController quixoController;
    private QuixoActionController  quixoActionController;
    private MenuItem help, start, intro, quit;
    private DialogView dialogView;
    private Optional<ButtonType> result;
    private ButtonType button;
    private View view;
    private Controller controller;
    private Model model;


    @BeforeAll
    public static void initToolkit() {
        new JFXPanel();
    }


    @BeforeEach
    public void setup() {
        view = mock(View.class);
        quixoView = mock(QuixoView.class);
        view = quixoView;

        controller = mock(Controller.class);
        quixoController = mock(QuixoController.class);
        controller = quixoController;

        model = mock(Model.class);

        help = new MenuItem();
        start = new MenuItem();
        intro = new MenuItem();
        quit = new MenuItem();
        dialogView = mock(DialogView.class);
        button = new ButtonType("");
//        dialogView = new DialogView(null);
        result = Optional.of(button);

        when(quixoView.getMenuQuit()).thenReturn(quit);
        when(quixoView.getMenuHelp()).thenReturn(help);
        when(quixoView.getMenuStart()).thenReturn(start);
        when(quixoView.getMenuIntro()).thenReturn(intro);

        quixoActionController = new QuixoActionController(null, view, controller) {
            @Override
            public DialogView createDialogView() {
                return dialogView;
            }
        };
    }


    @Test
    public void TestSetMenuHandlers(){
        when(dialogView.initDialog()).thenReturn(result);
        when(dialogView.getButtonTypeJouer()).thenReturn(button);
        quixoActionController.setMenuHandlers();

        help.getOnAction().handle(new ActionEvent());
        verify(dialogView).initVBox();


        when(dialogView.getGameMode()).thenReturn(1);
        when(model.isStageStarted()).thenReturn(false);
        start.getOnAction().handle(new ActionEvent());
        verify(dialogView).initVBox();
        verify(controller).stopStage();




        intro.getOnAction().handle(new ActionEvent());
        verify(controller).stopGame();
        verify(view).resetView();

//        quit.getOnAction().handle(new ActionEvent());     // Stopped
//        verify(system).exit(0);
    }


}
