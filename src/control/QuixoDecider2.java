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
    QuixoController quixoController = (QuixoController) control;

    final int ALIGN3 = 50;
    final int ALIGN4 = 1000;
    final int ALIGN5 = 10000;
    int scoreFinal;
    int[][] coordBestMove;



    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();
        scoreFinal =-100000;
        coordBestMove = new int[2][2];


        board = stage.getBoard(); // get the board

        int[][] tabBoard = copyBoardInTab();
        List<Point> allMoves = getAllmoves();
        System.out.println(allMoves);

        for (int i = 0; i<allMoves.size(); i=i+2){

            // recuperer le X et le Y du premier coup (from -> donc là ou on va prendre le cube)
            int fromCol = (int) allMoves.get(i).getX();
            int fromRow = (int) allMoves.get(i).getY();

            // recuperer le X et le Y du deuxième coup (insertion -> donc là ou on va poser le cube pour ensuite bouger tout les autres)
            int insertionCol = (int) allMoves.get(i+1).getX();
            int insertionRow = (int) allMoves.get(i+1).getY();

            int[][] tabAfterMove = moveSequenceCube(insertionRow, insertionCol, fromRow, fromCol);
            int score = getScore(2, tabAfterMove) - (getScore(1, tabAfterMove)*2);
            if(score > scoreFinal) {
                scoreFinal = score;
                fromRow = (int) allMoves.get(i).getY();
                fromCol = (int) allMoves.get(i).getX();
                insertionRow = (int) allMoves.get(i+1).getY();
                insertionCol = (int) allMoves.get(i+1).getX();

                coordBestMove[0] = new int[]{fromRow, fromCol};
                coordBestMove[1] = new int[]{insertionRow, insertionCol};

                System.out.println("coordBestMove" + Arrays.deepToString(coordBestMove) + " scoreFinal : " + scoreFinal);
            }
//            System.out.println("Tableau de base : ");
//            afficheTab2D(tabBoard);
//            System.out.println(new StringBuilder().append("déplacement de : ").append(fromRow).append(fromCol));
//            System.out.println(new StringBuilder().append("destination : ").append(insertionRow).append(insertionCol));
//            System.out.println("Tableau après le coup : ");
//            afficheTab2D(tabAfterMove);
        }

        Cube cube = (Cube) board.getElement(coordBestMove[0][0], coordBestMove[0][1]);// row -- col

        cube.setFace(2);
//        ActionList firstMove = ActionFactory.generatePutInContainer(model, cube, "cubepot", coordBestMove[0][0], coordBestMove[0][1]);


        System.out.println("Je prends l'element : "+coordBestMove[0][0] + coordBestMove[0][1]);
//        cube = (Cube) pot.getElement(0, 0);
        System.out.println("Element deplacé dans le pot");
        quixoController.mooveSequenceCube(coordBestMove[1][0], coordBestMove[1][1], coordBestMove[0][0], coordBestMove[0][1], false); //insert row, insert col, from row, from col
        System.out.println("Element reposé en  : "+coordBestMove[1][0] + coordBestMove[1][1]);

        ActionList secondMove = ActionFactory.generatePutInContainer(model, cube, "quixoboard", coordBestMove[1][0], coordBestMove[1][1]); //rowdest, coldest

//        firstMove.addAll(secondMove);
        secondMove.setDoEndOfTurn(true);
        return secondMove;

    }

    public int getScore(int face, int[][] board) {
        int score = 0;

        // Vérifie les lignes et colonnes
        for (int i = 0; i < 5; i++) {
            int rowCount = 0;
            int colCount = 0;

            for (int j = 0; j < 5; j++) {
                if (board[i][j] == face) rowCount++;
                if (board[j][i] == face) colCount++;
            }

            score += getPointsForAlignment(rowCount);
            score += getPointsForAlignment(colCount);
        }

        // Diagonale principale
        int diag1 = 0;
        // Diagonale secondaire
        int diag2 = 0;

        for (int i = 0; i < 5; i++) {
            if (board[i][i] == face) diag1++;
            if (board[i][4 - i] == face) diag2++;
        }

        score += getPointsForAlignment(diag1);
        score += getPointsForAlignment(diag2);

        return score;
    }

    private int getPointsForAlignment(int count) {
        if(count == 3)
            return ALIGN3;
        else if (count == 4)
            return ALIGN4;
        else if (count == 5)
            return ALIGN5;
        return 0;
    }

    public int[][] bestScoreMove(){
        return null;
    }

    public int[][] moveSequenceCube(int insertionRow, int insertionCol, int fromRow, int fromCol) {
        // Créez une copie du tableau actuel
        int[][] tabBoardAfterMove = copyBoardInTab();

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
//                System.out.println("board.getElement"+ i + " " + j);
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
            int cubeRow = (int) valid.get(i).getY();// 0
            int cubeCol = (int) valid.get(i).getX(); // 4
            firstMove[0] = cubeRow;
            firstMove[1] = cubeCol;


            List<Point> secondMove = board.computeValidCells(false, firstMove , model);
            for(int j =0; j<secondMove.size(); j++){

                Point firstMoveCopy = new Point(firstMove[0], firstMove[1]);
                allMoves.add(firstMoveCopy);

                secondMoveTab[0] = (int) secondMove.get(j).getY();
                secondMoveTab[1] = (int) secondMove.get(j).getX();

                Point secondMoveCopy = new Point(secondMoveTab[0], secondMoveTab[1]);
                allMoves.add(secondMoveCopy);
            }
        }
        System.out.println(allMoves);
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
}