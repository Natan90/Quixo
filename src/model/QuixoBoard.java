package model;

import boardifier.control.Logger;
import boardifier.model.GameStageModel;
import boardifier.model.ContainerElement;
import boardifier.model.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.awt.*;

/**
 * quixo main board represent the element where pawns are put when played
 * Thus, a simple ContainerElement with 3 rows and 3 column is needed.
 * Nevertheless, in order to "simplify" the work for the controller part,
 * this class also contains method to determine all the valid cells to put a
 * pawn with a given value.
 */
public class QuixoBoard extends ContainerElement {
    public QuixoBoard(int x, int y, GameStageModel gameStageModel) {
        // call the super-constructor to create a 3x3 grid, named "quixoboard", and in x,y in space
        super("quixoboard", x, y, 5 , 5, gameStageModel);
    }

//    public boolean[][] getReachableCells() {
//        return getReachableCells();
//    }


    public List<Point> computeValidCells(boolean isFirstMove, int[] coordCube, Model model) {
        int size = 5;
        List<Point> lst = new ArrayList<>();
        Cube cube = null;

        QuixoStageModel gameStage = (QuixoStageModel) model.getGameStage();
        //recuperer le cube choisi dans le plateau
        ContainerElement board = gameStage.getBoard();

        //verifier que l'utilisateur a bien choisi une case sur l'exterieur du plateau

            for (int i = 0; i < size; i++) {
//                System.out.println("i " + i);
                for (int j = 0; j < size; j++) {

//                    System.out.println("j " + j);
                    cube = (Cube) board.getElement(i, j);

                    //On vérifie si le cube est null (utile pour les couleurs quand on enleve le cube du board et qu'on le met dans la poule)
                    if (cube == null) {
//                        System.out.println("cube = null");
                        continue;  // Si le cube est nul, passer à la prochaine itération
                    }

                    // col = j et row = i
                    //Cas toujours invalide que ce soit dans les 2 moves
                    if (i > 0 && j < 4 && j > 0 && i < 4 ) {
//                        System.out.println("Dans le carré du milieu");
                        continue;
                    }

                    if (isFirstMove){

                        if ((cube.getFace() == 2 && model.getIdPlayer() == 0) || (cube.getFace() == 1 && model.getIdPlayer() == 1)) {
//                            System.out.println("if de la face avec le player");
                            continue;
                        }

                    }else {

                        //verifier si on joue bien sur la ligne ou colonne correspondante
                        if (j != coordCube[0] && i != coordCube[1]) {
//                            System.out.println("Dans la verif tableau coup bon");
                            continue;
                        }
                        //verifier si c'est la meme position
                        if (j == coordCube[0] && i == coordCube[1]) {
//                            System.out.println("Position exacte");
                            continue;
                        }
                        //verifier si c'est la bonne saisie et comprise entre 1 et 3 inclus
                        if ((j == coordCube[0] && i > 0 && i < 4) || (i == coordCube[1] && j > 0 && j < 4)) {
//                            System.out.println("dans le truc de baisé");
                            continue;
                        }

                    }
                    lst.add(new Point(i, j));
                }
            }
        return lst;
    }
}
