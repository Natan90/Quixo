package model;

import boardifier.model.*;
import view.WinnerScreen;

/**
 * HoleStageModel defines the model for the single stage in "The Hole". Indeed,
 * there are no levels in this game: a party starts and when it's done, the game is also done.
 *
 * HoleStageModel must define all that is needed to manage a party : state variables and game elements.
 * In the present case, there are only 2 state variables that represent the number of pawns to play by each player.
 * It is used to detect the end of the party.
 * For game elements, it depends on what is chosen as a final UI design. For that demo, there are 12 elements used
 * to represent the state : the main board, 2 pots, 8 pawns, and a text for current player.
 *
 * WARNING ! HoleStageModel DOES NOT create itself the game elements because it would prevent the possibility to mock
 * game element classes for unit testing purposes. This is why HoleStageModel just defines the game elements and the methods
 * to set this elements.
 * The instanciation of the elements is done by the HoleStageFactory, which uses the provided setters.
 *
 * HoleStageModel must also contain methods to check/modify the game state when given events occur. This is the role of
 * setupCallbacks() method that defines a callback function that must be called when a pawn is put in a container.
 * This is done by calling onPutInContainer() method, with the callback function as a parameter. After that call, boardifier
 * will be able to call the callback function automatically when a pawn is put in a container.
 * NB1: callback functions MUST BE defined with a lambda expression (i.e. an arrow function).
 * NB2:  there are other methods to defines callbacks for other events (see onXXX methods in GameStageModel)
 * In "The Hole", everytime a pawn is put in the main board, we have to check if the party is ended and in this case, who is the winner.
 * This is the role of computePartyResult(), which is called by the callback function if there is no more pawn to play.
 *
 */
public class QuixoStageModel extends GameStageModel {

    public final static int STATE_SELECTCUBE = 1; // the player must select a pawn
    public final static int STATE_SELECTDEST = 2; // the player must select a destination

    // define stage state variables

    public final static int STATE_SELECTEDCUBE = 1;
    public final static int STATE_SELECTEDDEST = 2;


    // define stage game elements
    private QuixoBoard board;
    private QuixoPawnPot blackPot;
    private QuixoPawnPot redPot;
    private Cube[] cubes;
    private TextElement playerName;
//    private ContainerElement mainContainer;

    //Uncomment this 2 methods if example with a main container is used
//    public ContainerElement getMainContainer() {
//        return mainContainer;
//    }
//
//    public void setMainContainer(ContainerElement mainContainer) {
//        this.mainContainer = mainContainer;
//        addContainer(mainContainer);
//    }


    public QuixoStageModel(String name, Model model) {
        super(name, model);
        state = STATE_SELECTCUBE;
//        setupCallbacks();
        state = STATE_SELECTEDCUBE;
    }

    public QuixoBoard getBoard() {
        return board;
    }
    public void setBoard(QuixoBoard board) {
        this.board = board;
        addContainer(board);
    }

    public QuixoPawnPot getRedPot() {
        return redPot;
    }
    public void setRedPot(QuixoPawnPot redPot) {
        this.redPot = redPot;
        addContainer(redPot);
    }

    public Cube[] getCubes() {
        return cubes;
    }
    public void setCubes(Cube[] cubes){
        this.cubes = cubes;
        for (int i=0; i< cubes.length; i++) {
            addElement(cubes[i]);
            System.out.println("Cubes added to stage model" + cubes[i]);
        }
    }

    public TextElement getPlayerName() {
        return playerName;
    }
    public void setPlayerName(TextElement playerName) {
        this.playerName = playerName;
        addElement(playerName);
    }




    public void setupCallbacks(ContainerElement board) {
        System.out.println("setupCallbacks appelée");
        int size = 5;
        int face;
        boolean allSame;

        // Vérif lignes
        for (int i = 0; i < size; i++) {
            Cube cube = (Cube) board.getElement(i, 0);
            if (cube == null || cube.getFace() == 0) continue;

            face = cube.getFace();
            allSame = true;
            for (int j = 1; j < size; j++) {
                Cube c = (Cube) board.getElement(i, j);
                if (c == null || c.getFace() != face) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) computePartyResult(face);
        }

        // Vérif colonnes
        for (int j = 0; j < size; j++) {
            Cube cube = (Cube) board.getElement(0, j);
            if (cube == null || cube.getFace() == 0) continue;

            face = cube.getFace();
            allSame = true;
            for (int i = 1; i < size; i++) {
                Cube c = (Cube) board.getElement(i, j);
                if (c == null || c.getFace() != face) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) computePartyResult(face);
        }

        // Diagonale principale
        Cube diag1 = (Cube) board.getElement(0, 0);
        if (diag1 != null && diag1.getFace() != 0) {
            face = diag1.getFace();
            allSame = true;
            for (int i = 1; i < size; i++) {
                Cube c = (Cube) board.getElement(i, i);
                if (c == null || c.getFace() != face) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) computePartyResult(face);
        }

        // Diagonale secondaire
        Cube diag2 = (Cube) board.getElement(0, size - 1);
        if (diag2 != null && diag2.getFace() != 0) {
            face = diag2.getFace();
            allSame = true;
            for (int i = 1; i < size; i++) {
                Cube c = (Cube) board.getElement(i, size - 1 - i);
                if (c == null || c.getFace() != face) {
                    allSame = false;
                    break;
                }
            }
            if (allSame) computePartyResult(face);
        }
    }


    public void computePartyResult(int face) {
        int idWinner = -1;
        if( face == 1 ){
            System.out.println("Le winner est 1 avec id = 0");

            idWinner = 0;
        }
        if( face == 2 ){
            System.out.println("Le winner est 2 avec id = 1");
            idWinner = 1;
        }
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();

//        WinnerScreen winnerScreen = new WinnerScreen(view);
//        winnerScreen.display();


    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new QuixoStageFactory(this);
    }
}