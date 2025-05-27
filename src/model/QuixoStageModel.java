package model;

import boardifier.model.*;
import control.QuixoController;

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

    // define stage state variables
    private int blackPawnsToPlay;
    private int redPawnsToPlay;

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
//        setupCallbacks();
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




    public void setupCallbacks(ContainerElement board, int currentPlayer) {



        int size = 5;
        int face;
        boolean allSame;

        // Vérification des lignes
        for (int i = 0; i < size; i++) {
            System.out.println("Vérification des lignes");
            // On récupère la face du premier cube
            Cube cube = (Cube) board.getElement(i, 0);
            if (cube == null)
                continue;

            face = cube.getFace();
            //Si face blanche, on return false directement
            allSame = true;
            for (int j = 1; j < size; j++) {
                if(face == 0){
                    allSame = false;
                    break;
                }
                // On compare avec les cubes à la suite
                Cube cube1 = (Cube) board.getElement(i, j);
                if (cube1 == null)
                    continue;
                if (cube1.getFace() != face) {
                    allSame = false;
                    break;
                }

            }
            if (allSame) {
                if (currentPlayer == 1)
                    computePartyResult(face);
//                if (model.getCurrentPlayer().getType() + 1 != face) {
//                    computePartyResult(face);
//                }
            }

        }

        // Vérification des colonnes
        for (int j = 0; j < size; j++) {
            System.out.println("Vérification des colonnes");
            // On récupère la face du premier cube
            Cube cube = (Cube) board.getElement(0, j);
            if (cube == null)
                continue;

            face = cube.getFace();
            allSame = true;
            for (int i = 1; i < size; i++) {
                if (face == 0) {
                    allSame = false;
                    break;
                }
                // On compare avec les cubes à la suite
                Cube cube1 = (Cube) board.getElement(i, j);
                if (cube1 == null)
                    continue;
                if (cube1.getFace() != face) {
                    allSame = false;
                    break;
                }

            }
            if (allSame) {
                Player joueurFace = model.getPlayers().get(face - 1);
                if (joueurFace.getType() != face - 1) {
                    computePartyResult(face);
                }
//                if (model.getCurrentPlayer().getType() + 1 != face) {
//                    computePartyResult(face);
//                }
            }

            // Diagonale principale (0,0 à 4,4)
            // On récupère la face du premier cube
            Cube cube1 = (Cube) board.getElement(0, 0);
            if (cube1 == null)
                continue;
            face = cube1.getFace();

            boolean allSameDiag1 = true;
            for (int i = 1; i < size; i++) {
//                System.out.println("Vérification première diago");
                if (face == 0) {
                    allSameDiag1 = false;
                    break;
                }
                // On compare avec les cubes à la suite
                Cube cube2 = (Cube) board.getElement(i, i);
                if (cube2 == null)
                    continue;
                if (cube2.getFace() != face) {
                    allSameDiag1 = false;
                    break;
                }
            }
            if (allSameDiag1) {
                Player joueurFace = model.getPlayers().get(face - 1);
                if (joueurFace.getType() != face - 1) {
                    computePartyResult(face);
                }
//                if (model.getCurrentPlayer().getType() + 1 != face) {
//                    computePartyResult(face);
//                }
            }

            // Diagonale secondaire (0,4 à 4,0)
            // On récupère la face du premier cube
            Cube cube2 = (Cube) board.getElement(0, size - 1);
            if (cube2 == null)
                continue;
            face = cube2.getFace();
            boolean allSameDiag2 = true;

            for (int i = size - 1; i > 1; i--) {
//                System.out.println("Vérification deuxième diago");
                if (face == 0) {
                    allSameDiag2 = false;
                    break;
                }
                // On compare avec les cubes à la suite
                Cube cube3 = (Cube) board.getElement(i, size - 1 - i);
                if (cube3 == null)
                    continue;
                if (cube3.getFace() != face) {
                    allSameDiag2 = false;
                    break;
                }

            }
            if (allSameDiag2) {
                if (currentPlayer == face) {
                    computePartyResult(face);
                }
//                if (model.getCurrentPlayer().getType() + 1 != face) {
//                    computePartyResult(face);
//                }
            }

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


    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new QuixoStageFactory(this);
    }
}