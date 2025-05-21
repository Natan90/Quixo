package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.io.FilterOutputStream;
import java.util.*;
import java.util.List;

public class QuixoDecider2 extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider2(Model model, Controller control) {
        super(model, control);
    }

    int max;
    QuixoBoard board;
    QuixoStageModel stageModel;
    ActionList firstAction;


    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();


        board = stage.getBoard(); // get the board

        ContainerElement pot = null;
        pot = stage.getRedPot();

        int[][] tabBoard = copyBoardInTab();
        List<Point> allMoves = getAllmoves();

        for (int i = 0; i<allMoves.size(); i=i+2){

            // recuperer le X et le Y du premier coup (from -> donc là ou on va prendre le cube)
            int fromCol = (int) allMoves.get(i).getX();
            int fromRow = (int) allMoves.get(i).getY();

            // recuperer le X et le Y du deuxième coup (insertion -> donc là ou on va poser le cube pour ensuite bouger tout les autres)
            int insertionCol = (int) allMoves.get(i+1).getX();
            int insertionRow = (int) allMoves.get(i+1).getY();




            int[][] tabAfterMove = moveSequenceCube(insertionRow, insertionCol, fromRow, fromCol);
            System.out.println("Tableau de base : ");
            afficheTab2D(tabBoard);
            System.out.println(new StringBuilder().append("déplacement de : ").append(fromCol).append(fromRow));
            System.out.println(new StringBuilder().append("destination : ").append(insertionCol).append(insertionRow));
            System.out.println("Tableau après le coup : ");
            afficheTab2D(tabAfterMove);

            // ce qu'il faut faire -> completer la méthode moveSequenceCube qui nous retourne l'etat du tableau après avoir tout bougé
            // ensuite appeler une nouvelle méthode pour calculer "l'éfficacité" du coup en cours
            // stocker le resultat qqpart
            // écraser le tableau ou on a bougé les cubes
            // repartir du tableau tabBoard
            // jouer les prochains coups


        }





        return null;

    }

    public int[][] moveSequenceCube(int insertionRow, int insertionCol, int fromRow, int fromCol) {
        // Créez une copie du tableau actuel
        int[][] tabBoardAfterMove = copyBoardInTab();
//        int[][] tabBoardAfterMove = Arrays.stream(copyBoardInTab())
//                .map(int[]::clone)
//                .toArray(int[][]::new);

        if (insertionCol == fromCol) {
            if (insertionRow < fromRow) {
                // Déplacement colonne vers le bas
                for (int i = fromRow; i > insertionRow; i--) {
                    tabBoardAfterMove[i][fromCol] = tabBoardAfterMove[i - 1][fromCol];
                }
            } else {
                // Déplacement colonne vers le haut
                for (int i = fromRow; i < insertionRow; i++) {
                    tabBoardAfterMove[i][fromCol] = tabBoardAfterMove[i + 1][fromCol];
                }
            }
        }

        if (insertionRow == fromRow) {
            if (insertionCol < fromCol) {
                // Déplacement ligne vers la droite
                for (int i = fromCol; i > insertionCol; i--) {
                    tabBoardAfterMove[fromRow][i] = tabBoardAfterMove[fromRow][i - 1];
                }
            } else {
                // Déplacement ligne vers la gauche
                for (int i = fromCol; i < insertionCol; i++) {
                    tabBoardAfterMove[fromRow][i] = tabBoardAfterMove[fromRow][i + 1];
                }
            }
        }

        tabBoardAfterMove[insertionRow][insertionCol] = 2; // Face du joueur 2

        return tabBoardAfterMove;
    }

    public int[][] copyBoardInTab(){
        int[][] tabBoard = new int[5][5];
        for (int i =0; i<5; i++){
            for (int j =0; j<5; j++){
                //recuperer la face du cube parcouru
                Cube cube = (Cube) board.getElement(i, j);
                //la mettre dans le tableau
                tabBoard[i][j] = cube.getFace();
            }
        }
        return tabBoard;
    }

    public List<Point> getAllmoves(){
        List<Point> valid = board.computeValidCells(true, null, model);
        // 0 4 -> A5

        int[] firstMove = new int[2];// [x, y]
        List<Point> allMoves = new ArrayList<>(); // [[x, y], [x, y]]
        int[] secondMoveTab = new int[2];


        for(int i=0; i<valid.size(); i++){
            int cubeRow = (int) valid.get(i).getX();// 0
            int cubeCol = (int) valid.get(i).getY(); // 4
            firstMove[0] = cubeRow;
            firstMove[1] = cubeCol;


            List<Point> secondMove = board.computeValidCells(false, firstMove , model);
            for(int j =0; j<secondMove.size(); j++){

                Point firstMoveCopy = new Point(firstMove[0], firstMove[1]);
                allMoves.add(firstMoveCopy);

                secondMoveTab[0] = (int) secondMove.get(j).getX();
                secondMoveTab[1] = (int) secondMove.get(j).getY();

                Point secondMoveCopy = new Point(secondMoveTab[0], secondMoveTab[1]);
                allMoves.add(secondMoveCopy);
            }
        }
        return allMoves;
    }
    public void afficheTab2D(int[][] tab){
//        System.out.println("Tableau : \n");
        for(int i =0; i<tab.length;i++){
            for (int j=0; j<tab.length; j++){
                System.out.print(tab[i][j]);
            }
            System.out.println("");
        }

    }


// CODE REUTILISABLE POUR PARCOURIR CHAQUE ELEM DE LA LISTE DES MOVES, ET JOUER LE COUP DANS UNE ACTIONLIST
//    // first coup
//    int row = (int) allMoves.get(i).getY();
//    int col = (int) allMoves.get(i).getX();
//    Cube cube = (Cube) simulatedBoard.getElement(row, col);
//
//
//    firstAction = ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0);
//            System.out.println("firstAction " + firstAction + "cube pris" + row + " " + col);
//            System.out.println("posé dans la grille cubepot" + 0 + " " + 0);
//
//    //second coup
//    row = (int) allMoves.get(i+1).getY();
//    col = (int) allMoves.get(i+1).getX();
//
//    cube = (Cube) pot.getElement(0, 0);
//
//    ActionList secondAction = ActionFactory.generatePutInContainer(model, cube, "simulatedBoard", row, col);
//            System.out.println("secondAction " + secondAction + "cube pris" + row + " " + col);
//            System.out.println("posé dans la grille simulatedBoard" + row + " " + col);
//
//
//
//    //jouer tout les coups possibles sur le plateau simulé en parcourant la liste allMoves
//    //dans le premier element de la liste il y a le premier coup et le deuxième, le deuxième coup
//    //appeler getAlignement() ou une autre méthode pour calculer le "rendement" de chaque coup
//    //jouer le coup le plus intéressant sur le VRAI plateau de jeu
//
//            firstAction.addAll(secondAction);
//            firstAction.setDoEndOfTurn(false);
}