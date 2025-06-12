package control;

import boardifier.control.ActionFactory;
import boardifier.control.ActionPlayer;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.ContainerElement;
import boardifier.model.Coord2D;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import boardifier.model.animation.AnimationTypes;
import boardifier.view.GridLook;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.io.FilterOutputStream;
import java.util.*;
import java.util.List;

public class QuixoDecider3 extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider3(Model model, Controller control) {
        super(model, control);
    }

    QuixoBoard board;
    QuixoStageModel stageModel;
    QuixoController quixoController = (QuixoController) control;

    final int ALIGN3 = 50;
    final int ALIGN4 = 1000;
    final int ALIGN5 = 10000;
    final int MAX_DEPTH = 4;
    final int INFINITY = 100000;

    int[][] coordBestMove;

    static class Move {
        int fromRow, fromCol;
        int insertionRow, insertionCol;
        int score;

        Move(int fromRow, int fromCol, int insertionRow, int insertionCol) {
            this.fromRow = fromRow;
            this.fromCol = fromCol;
            this.insertionRow = insertionRow;
            this.insertionCol = insertionCol;
            this.score = 0;
        }

        @Override
        public String toString() {
            return String.format("Move[(%d,%d)->(%d,%d), score=%d]",
                    fromRow, fromCol, insertionRow, insertionCol, score);
        }
    }

    @Override
    public ActionList decide() {
        System.out.println("Decider 3 - Minimax");
        System.out.println("getIdPlayer " + model.getIdPlayer());

        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();
        board = stage.getBoard();
        coordBestMove = new int[2][2];

        int[][] currentBoard = copyBoardInTab();
        afficheTab2D(currentBoard);

        int currentPlayer = (model.getIdPlayer() == 0) ? 1 : 2;
        Move bestMove = minimax(currentBoard, MAX_DEPTH, true, -INFINITY, INFINITY, currentPlayer);

        if (bestMove == null) {
            System.err.println("Aucun coup valide trouvé ! Utilisation d'un coup de secours...");
            List<Move> availableMoves = getAllPossibleMoves(currentBoard, currentPlayer);
            if (!availableMoves.isEmpty()) {
                bestMove = availableMoves.get(0);
                System.out.println("Coup de secours utilisé : " + bestMove);
            } else {
                System.err.println("Aucun coup disponible du tout !");
                return null;
            }
        }

        System.out.println("Meilleur coup trouvé : " + bestMove);

        coordBestMove[0] = new int[]{bestMove.fromRow, bestMove.fromCol};
        coordBestMove[1] = new int[]{bestMove.insertionRow, bestMove.insertionCol};

        return executeBestMove();
    }


    private Move minimax(int[][] board, int depth, boolean isMaximizing, int alpha, int beta, int currentPlayer) {
        if (depth == 0 || isGameFinished(board)) {
            Move move = new Move(0, 0, 0, 0);
            move.score = evaluateBoard(board, (model.getIdPlayer() == 0) ? 1 : 2);
            return move;
        }

        List<Move> possibleMoves = getAllPossibleMoves(board, currentPlayer);

        if (possibleMoves.isEmpty()) {
            Move move = new Move(0, 0, 0, 0);
            move.score = isMaximizing ? -INFINITY : INFINITY;
            return move;
        }

        Move bestMove = null;

        if (isMaximizing) {
            int maxEval = -INFINITY;

            for (Move move : possibleMoves) {
                int[][] newBoard = simulateMove(board, move, currentPlayer);
                int nextPlayer = (currentPlayer == 1) ? 2 : 1;

                Move eval = minimax(newBoard, depth - 1, false, alpha, beta, nextPlayer);

                if (eval.score > maxEval) {
                    maxEval = eval.score;
                    bestMove = new Move(move.fromRow, move.fromCol, move.insertionRow, move.insertionCol);
                    bestMove.score = maxEval;
                }

                alpha = Math.max(alpha, eval.score);
                if (beta <= alpha) {
                    break;
                }
            }
        } else {
            int minEval = INFINITY;

            for (Move move : possibleMoves) {
                int[][] newBoard = simulateMove(board, move, currentPlayer);
                int nextPlayer = (currentPlayer == 1) ? 2 : 1;

                Move eval = minimax(newBoard, depth - 1, true, alpha, beta, nextPlayer);

                if (eval.score < minEval) {
                    minEval = eval.score;
                    bestMove = new Move(move.fromRow, move.fromCol, move.insertionRow, move.insertionCol);
                    bestMove.score = minEval;
                }

                beta = Math.min(beta, eval.score);
                if (beta <= alpha) {
                    break;
                }
            }
        }

        return bestMove;
    }

    private int evaluateBoard(int[][] board, int player) {
        int opponent = (player == 1) ? 2 : 1;

        int playerScore = getScore(player, board);
        int opponentScore = getScore(opponent, board);

        // Si le joueur a gagné
        if (hasWon(board, player)) {
            return INFINITY;
        }
        if (hasWon(board, opponent)) {
            return -INFINITY;
        }

        return playerScore - (opponentScore * 2);
    }


    private boolean hasWon(int[][] board, int player) {
        // Vérifier les lignes
        for (int i = 0; i < 5; i++) {
            boolean rowWin = true;
            for (int j = 0; j < 5; j++) {
                if (board[i][j] != player) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) return true;
        }

        // Vérifier les colonnes
        for (int j = 0; j < 5; j++) {
            boolean colWin = true;
            for (int i = 0; i < 5; i++) {
                if (board[i][j] != player) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) return true;
        }

        boolean diag1Win = true, diag2Win = true;
        for (int i = 0; i < 5; i++) {
            if (board[i][i] != player) diag1Win = false;
            if (board[i][4-i] != player) diag2Win = false;
        }

        return diag1Win || diag2Win;
    }

    private boolean isGameFinished(int[][] board) {
        return hasWon(board, 1) || hasWon(board, 2);
    }


    private List<Move> getAllPossibleMoves(int[][] board, int currentPlayer) {
        List<Move> moves = new ArrayList<>();

        List<Point> validCells = getValidFirstMoves(board);

        for (Point firstMove : validCells) {
            int fromRow = (int) firstMove.getY();
            int fromCol = (int) firstMove.getX();

            List<Point> secondMoves = getValidSecondMoves(board, fromRow, fromCol);

            for (Point secondMove : secondMoves) {
                int insertionRow = (int) secondMove.getY();
                int insertionCol = (int) secondMove.getX();

                moves.add(new Move(fromRow, fromCol, insertionRow, insertionCol));
            }
        }

        return moves;
    }

    private List<Point> getValidFirstMoves(int[][] board) {
        List<Point> validMoves = new ArrayList<>();
        int currentPlayer = (model.getIdPlayer() == 0) ? 1 : 2;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (i == 0 || i == 4 || j == 0 || j == 4) {
                    int face = board[i][j];
                    if (face == 0 || face == currentPlayer) {
                        Cube cube = (Cube) this.board.getElement(i, j);
                        if (cube != null && cube.getContainer() != null) {
                            validMoves.add(new Point(j, i)); // Point(x=col, y=row)
                        }
                    }
                }
            }
        }

        System.out.println("Coups valides trouvés : " + validMoves.size());
        return validMoves;
    }


    private List<Point> getValidSecondMoves(int[][] board, int fromRow, int fromCol) {
        List<Point> validMoves = new ArrayList<>();

        if ((fromRow == 0 || fromRow == 4) && (fromCol == 0 || fromCol == 4)) {
            if (fromRow == 0) {
                validMoves.add(new Point(fromCol, 4)); // Même colonne, ligne opposée
            } else {
                validMoves.add(new Point(fromCol, 0)); // Même colonne, ligne opposée
            }

            if (fromCol == 0) {
                validMoves.add(new Point(4, fromRow)); // Même ligne, colonne opposée
            } else {
                validMoves.add(new Point(0, fromRow)); // Même ligne, colonne opposée
            }
        }
        else if (fromRow == 0 || fromRow == 4) {
            validMoves.add(new Point(fromCol, fromRow == 0 ? 4 : 0));
        }
        else if (fromCol == 0 || fromCol == 4) {
            validMoves.add(new Point(fromCol == 0 ? 4 : 0, fromRow));
        }

        return validMoves;
    }


    private int[][] simulateMove(int[][] board, Move move, int player) {
        int[][] newBoard = new int[5][5];

        for (int i = 0; i < 5; i++) {
            System.arraycopy(board[i], 0, newBoard[i], 0, 5);
        }

        return moveSequenceCubeOnArray(newBoard, move.insertionRow, move.insertionCol,
                move.fromRow, move.fromCol, player);
    }

    private int[][] moveSequenceCubeOnArray(int[][] board, int insertionRow, int insertionCol,
                                            int fromRow, int fromCol, int player) {
        if (insertionCol == fromCol) {
            if (insertionRow < fromRow) {
                for (int i = fromRow; i > insertionRow; i--) {
                    board[i][fromCol] = board[i - 1][fromCol];
                }
            } else {
                for (int i = fromRow; i < insertionRow; i++) {
                    board[i][fromCol] = board[i + 1][fromCol];
                }
            }
        }

        if (insertionRow == fromRow) {
            if (insertionCol < fromCol) {
                for (int i = fromCol; i > insertionCol; i--) {
                    board[fromRow][i] = board[fromRow][i - 1];
                }
            } else {
                for (int i = fromCol; i < insertionCol; i++) {
                    board[fromRow][i] = board[fromRow][i + 1];
                }
            }
        }

        board[insertionRow][insertionCol] = player;
        return board;
    }

    private void setBoardFromArray(int[][] array) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cube cube = (Cube) board.getElement(i, j);
                if (cube.getFace() != array[i][j]) {
                    cube.setFace(array[i][j]);
                }
            }
        }
    }

    private ActionList executeBestMove() {
        System.out.println("Tentative d'exécution du coup : " + Arrays.toString(coordBestMove[0]) + " -> " + Arrays.toString(coordBestMove[1]));

        // Vérifications de sécurité
        if (coordBestMove[0][0] < 0 || coordBestMove[0][0] >= 5 ||
                coordBestMove[0][1] < 0 || coordBestMove[0][1] >= 5) {
            System.err.println("Indices invalides pour le déplacement : " + Arrays.toString(coordBestMove[0]));
            return null;
        }

        Cube cube = (Cube) board.getElement(coordBestMove[0][0], coordBestMove[0][1]);

        if (cube == null) {
            System.err.println("Erreur : aucun cube trouvé à la position " + Arrays.toString(coordBestMove[0]));
            return null;
        }

        if (cube.getContainer() == null) {
            System.err.println("Erreur : le cube n'a pas de conteneur avant le déplacement !");
            cube.setContainer(board);
        }

        if (cube.getContainer() != board) {
            System.err.println("Erreur : le cube n'appartient pas au plateau !");
            return null;
        }

        if (model.getIdPlayer() == 1)
            cube.setFace(2);
        else if (model.getIdPlayer() == 0) {
            cube.setFace(1);
        }

        System.out.println("Je prends l'element : " + coordBestMove[0][0] + "," + coordBestMove[0][1]);

        quixoController.mooveSequenceCube(coordBestMove[1][0], coordBestMove[1][1],
                coordBestMove[0][0], coordBestMove[0][1], false);

        System.out.println("Element reposé en : " + coordBestMove[1][0] + "," + coordBestMove[1][1]);

        return ActionFactory.generatePutInContainer(control, model, cube, "quixoboard",
                coordBestMove[1][0], coordBestMove[1][1]);
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

    public int getPointsForAlignment(int count) {
        if (count == 3) {
            return ALIGN3;
        } else if (count == 4) {
            return ALIGN4;
        } else if (count == 5) {
            return ALIGN5;
        }
        return 0;
    }

    public int[][] copyBoardInTab() {
        int[][] tabBoard = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cube cube = (Cube) board.getElement(i, j);
                if (cube != null && cube.getContainer() != null) {
                    tabBoard[i][j] = cube.getFace();
                } else {
                    tabBoard[i][j] = 0; // Case vide si cube absent
                    System.err.println("Attention : cube absent à la position [" + i + "," + j + "]");
                }
            }
        }
        return tabBoard;
    }

    public void afficheTab2D(int[][] tab) {
        for (int i = 0; i < tab.length; i++) {
            for (int j = 0; j < tab.length; j++) {
                System.out.print(tab[i][j]);
            }
            System.out.println("");
        }
    }
}