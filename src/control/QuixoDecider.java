package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.ContainerElement;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class QuixoDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider(Model model, Controller control) {
        super(model, control);
    }

    QuixoBoard board;

    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel)model.getGameStage();
        board = stage.getBoard(); // get the board
        ContainerElement pot = null; // the pot where to take a pawn
        pot = stage.getRedPot();

        int rowDest = 0; // the dest. row in board
        int colDest = 0; // the dest. col in board

        int[] coordCube = new int[2];

        // Liste des coins. Si aucun coup n'est interessant à jouer, le bot jouera dans les coins
        List<Point> coins = List.of(new Point(0, 0), new Point(0,4), new Point(4, 0), new Point(4, 4));
        System.out.println("coins : " + coins.toString());

        List<Point> valid = board.computeValidCells(true, coordCube, model);
        //regarder si on ne peut pas aligner 5 cubes
        // Si non, essayer de creer la plus grande serie de cube possible
        // Si il n'y a pas de possibilité de faire une ligne de 3,


        List<List<Integer>> alignementsOpponent = new ArrayList<>(getAlignement(1));
        System.out.println(alignementsOpponent);

        List<List<Integer>> alignementsCurrentPlayer = new ArrayList<>(getAlignement(2));
        System.out.println(alignementsCurrentPlayer);

        List<Point> coordMaxInList = new ArrayList<>(searchMax(alignementsOpponent));
        System.out.println(coordMaxInList);



        //stratégie en deux temps
        // Si la liste adverse contient de gros alignements on va essayer de bloquer (à partir de 2 ou 3)
        // Sinon, on va essayer de compléter nos series de lignes
        // Si aucune option intéressante, prioriser les coins ou les cases blanches









        ActionList actions = ActionFactory.generatePutInContainer( model, null, "quixoboard", 0, 0);
        actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.

        return actions;
    }
    public boolean opponentIsWinning(){
        return false;

    }


    // Parcourir chaque colonne, chaque ligne et chaque diag et comptabiliser le nombre de "points" dedans
    public List<List<Integer>> getAlignement(int face) {
        int size = 5;
        int currentFace;

        List<Integer> lignes = new ArrayList<>();
        List<Integer> colonnes = new ArrayList<>();
        List<Integer> diagonales = new ArrayList<>();

        List<List<Integer>> alignements = new ArrayList<>();


        for (int i = 0; i < size; i++) {
            int compteur = 0;
//            System.out.println("Vérification des lignes");

            // Vérification des lignes
            for (int j = 0; j < size; j++) {
                currentFace = ((Cube) board.getElement(i, j)).getFace();

                if(face == currentFace)
                    compteur++;

            }
            lignes.add(compteur);


            // Vérification des colonnes
            compteur = 0;
            for (int j = 0; j < size; j++) {
                currentFace = ((Cube) board.getElement(j, i)).getFace();

                if(face == currentFace)
                    compteur++;
            }
            colonnes.add(compteur);
        }


        // Diagonales de (0,0) à (4,4)
        int j = 4;
        for (int i = 0; i < size; i++) {
            int compteur = 0;
            currentFace = ((Cube) board.getElement(i, j)).getFace();

            if (face == currentFace) {
                compteur++;
            }
            j--;

            if(i == size-1)
                diagonales.add(compteur);
        }

        // Diagonale secondaire de (0,4) à (4,0)

        j = 0;
        for (int i = 0; i < size; i++) {
            int compteur = 0;
                currentFace = ((Cube) board.getElement(i, j)).getFace();
                if (face == currentFace) {
                    compteur++;
                }
                j++;

            if(i == size-1)
                diagonales.add(compteur);
        }


        alignements.add(lignes);
        alignements.add(colonnes);
        alignements.add(diagonales);

        return alignements;

    }

    public List<Point> searchMax(List<List<Integer>> liste){

        List<Point> coordMax = new ArrayList<>();
        int max = 0;


        for (int i = 0; i < liste.size(); i++) {
            for (int j = 0; j < liste.get(i).size(); j++) {
                int elem = liste.get(i).get(j);

                if (elem > max) {
                    max = elem;
                    coordMax.clear();
                    coordMax.add(new Point(i, j));
                }
                else if (elem == max) {
                    coordMax.add(new Point(i, j));
                }

            }
        }


        return coordMax;
    }
}
