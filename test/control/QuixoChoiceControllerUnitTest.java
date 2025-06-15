package control;

import boardifier.model.TextElement;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.DialogView;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class QuixoChoiceControllerUnitTest {

    private DialogView dialogView;
    private QuixoChoiceController quixoChoiceController;
    private Button button;

    @BeforeAll
    public static void initToolkit() {
        new JFXPanel();
    }

    @BeforeEach
    public void setup() {
        dialogView = mock(DialogView.class);
        quixoChoiceController = new QuixoChoiceController(null, dialogView);

        when(dialogView.getApplyButton()).thenReturn(button);
    }

    @Test
    public void TestNameDisplayJcJ() {
        quixoChoiceController.nameDisplay(1);
        verify(dialogView).showJcJ();
        verify(dialogView).getApplyButton();


        assertTrue(button.isDisable());
    }

    @Test
    public void TestNameDisplayJcB(){
        quixoChoiceController.nameDisplay(2);
        verify(dialogView).showJcB();
        verify(dialogView).getApplyButton();

        assertTrue(button.isDisable());
    }


    @Test
    public void TestNameDisplayBvB() {
        quixoChoiceController.nameDisplay(3);
        verify(dialogView).showDefault();
        verify(dialogView, never()).getApplyButton();
    }


}
