package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class QuixoDecider3 extends Decider {

    private static final int ALIGN3 = 50;
    private static final int ALIGN4 = 1000;
    private static final int ALIGN5 = 10000;
    private static final int MAX_DEPTH = 2; // Profondeur de recherche

    private QuixoBoard board;
    private QuixoStageModel stageModel;
    private QuixoController quixoController;

    public QuixoDecider3(Model model, Controller control) {
        super(model, control);
        quixoController = (QuixoController) control;
    }

    @Override
    public ActionList decide() {
        System.out.println("QuixoDecider3 - IA anticipative");
        stageModel = (QuixoStageModel) model.getGameStage();
        board = stageModel.getBoard();

        int[][] currentBoard = copyBoardInTab();
        List<Point[]> allMoves = getAllMoves();

        int bestScore = Integer.MIN_VALUE;
        Point[] bestMove = null;

        for (Point[] move : allMoves) {
            int[][] boardAfterMove = simulateMove(currentBoard, move, model.getIdPlayer());
            int score = minimax(boardAfterMove, MAX_DEPTH - 1, false, model.getIdPlayer());
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        if (bestMove == null) return new ActionList(); // fallback

        int fromRow = bestMove[0].x;
        int fromCol = bestMove[0].y;
        int toRow = bestMove[1].x;
        int toCol = bestMove[1].y;

        Cube cube = (Cube) board.getElement(fromRow, fromCol);
        cube.setFace(model.getIdPlayer() == 0 ? 1 : 2);

        quixoController.mooveSequenceCube(toRow, toCol, fromRow, fromCol, false);

        return ActionFactory.generatePutInContainer(control, model, cube, "quixoboard", toRow, toCol);
    }

    private int minimax(int[][] boardState, int depth, boolean isMaximizing, int playerId) {
        if (depth == 0) {
            return evaluateBoard(boardState, playerId);
        }

        List<Point[]> moves = getAllMoves(boardState, isMaximizing ? playerId : 1 - playerId);
        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Point[] move : moves) {
            int[][] nextState = simulateMove(boardState, move, isMaximizing ? playerId : 1 - playerId);
            int score = minimax(nextState, depth - 1, !isMaximizing, playerId);
            bestScore = isMaximizing ? Math.max(score, bestScore) : Math.min(score, bestScore);
        }

        return bestScore;
    }

    private int evaluateBoard(int[][] board, int playerId) {
        int opponentId = (playerId == 0) ? 2 : 1;
        return getScore(playerId + 1, board) - 2 * getScore(opponentId, board);
    }

    private int[][] simulateMove(int[][] board, Point[] move, int playerId) {
        int[][] copy = deepCopy(board);
        return moveSequenceCube(copy, move[1].x, move[1].y, move[0].x, move[0].y, playerId + 1);
    }

    private int getScore(int face, int[][] board) {
        int score = 0;
        for (int i = 0; i < 5; i++) {
            int row = 0, col = 0;
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == face) row++;
                if (board[j][i] == face) col++;
            }
            score += getPointsForAlignment(row) + getPointsForAlignment(col);
        }

        int diag1 = 0, diag2 = 0;
        for (int i = 0; i < 5; i++) {
            if (board[i][i] == face) diag1++;
            if (board[i][4 - i] == face) diag2++;
        }
        return score + getPointsForAlignment(diag1) + getPointsForAlignment(diag2);
    }

    private int getPointsForAlignment(int count) {
        if (count == 5) return ALIGN5;
        if (count == 4) return ALIGN4;
        if (count == 3) return ALIGN3;
        return 0;
    }

    private int[][] moveSequenceCube(int[][] board, int insertRow, int insertCol, int fromRow, int fromCol, int face) {
        if (insertCol == fromCol) {
            if (insertRow < fromRow) {
                for (int i = fromRow; i > insertRow; i--) board[i][fromCol] = board[i - 1][fromCol];
            } else {
                for (int i = fromRow; i < insertRow; i++) board[i][fromCol] = board[i + 1][fromCol];
            }
        }
        if (insertRow == fromRow) {
            if (insertCol < fromCol) {
                for (int i = fromCol; i > insertCol; i--) board[fromRow][i] = board[fromRow][i - 1];
            } else {
                for (int i = fromCol; i < insertCol; i++) board[fromRow][i] = board[fromRow][i + 1];
            }
        }
        board[insertRow][insertCol] = face;
        return board;
    }

    private List<Point[]> getAllMoves() {
        return getAllMoves(copyBoardInTab(), model.getIdPlayer());
    }

    private List<Point[]> getAllMoves(int[][] boardState, int playerId) {
        List<Point> valid = board.computeValidCells(true, null, model);
        List<Point[]> allMoves = new ArrayList<>();

        for (Point p : valid) {
            int fromRow = p.y;
            int fromCol = p.x;
            int[] pos = {fromRow, fromCol};
            List<Point> secondMoves = board.computeValidCells(false, pos, model);

            for (Point dest : secondMoves) {
                allMoves.add(new Point[]{
                        new Point(fromRow, fromCol),
                        new Point(dest.y, dest.x)
                });
            }
        }
        return allMoves;
    }

    private int[][] copyBoardInTab() {
        int[][] tab = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Cube cube = (Cube) board.getElement(i, j);
                tab[i][j] = cube.getFace();
            }
        }
        return tab;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[5][5];
        for (int i = 0; i < 5; i++) {
            copy[i] = Arrays.copyOf(original[i], 5);
        }
        return copy;
    }
}
