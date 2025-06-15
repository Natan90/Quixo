import boardifier.control.Logger;
import boardifier.control.StageFactory;
import boardifier.model.Model;
import control.QuixoChoiceController;
import control.QuixoController;
import javafx.application.Application;
import javafx.stage.Stage;
import view.DialogView;
import view.QuixoRootPane;
import view.QuixoView;

public class QuixoConsole extends Application {

    private static int mode;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        Logger.setLevel(Logger.LOGGER_DEBUG); // show info and debug messages

        // create the global model
        Model model = new Model();
        // add some players taking mode into account
        // register a single stage for the game, called hole
        StageFactory.registerModelAndView("quixo", "model.QuixoStageModel", "view.QuixoStageView");
        // create the root pane, using the subclass HoleRootPane
        QuixoRootPane rootPane = new QuixoRootPane();
        // create the global view.
        QuixoView view = new QuixoView(model, stage, rootPane);
        // create the controllers.
        QuixoController control = new QuixoController(model,view);
        // set the name of the first stage to create when the game is started
        control.setFirstStageName("quixo");
        // set the stage title
        stage.setTitle("Quixo Game");
        // show the JavaFx main stag
        stage.show();
    }
}
