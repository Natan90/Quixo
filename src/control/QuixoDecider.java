package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.GameElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoPawnPot;
import model.QuixoStageModel;

import java.awt.*;
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
        QuixoPawnPot pot = null; // the pot where to take a pawn
        GameElement pawn = null; // the pawn that is moved
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
            // On récupère la face du premier cube
            //Si face blanche, on return false directement
            for (int j = 0; j < size; j++) {
                currentFace = ((Cube) board.getElement(i, j)).getFace();

                if(face == currentFace)
                    compteur++;

            }
            lignes.add(compteur);
        }

        // Vérification des colonnes
        for (int j = 0; j < size; j++) {
            int compteur = 0;
//            System.out.println("Vérification des colonnes");
            // On récupère la face du premier cube
            for (int i = 0; i < size; i++) {
                currentFace = ((Cube) board.getElement(i, j)).getFace();

                if(face == currentFace)
                    compteur++;
            }
            colonnes.add(compteur);
        }

        // Diagonales de (0,4) à (4,0)
        int j = 4;
        for (int i = 0; i < size; i++) {
            int compteur = 0;
            currentFace = ((Cube) board.getElement(i, j)).getFace();

            if (face == currentFace) {
                compteur++;
            }
            j--;

            diagonales.add(compteur);
        }

        // Diagonale secondaire (0,4 à 4,0)
        // On récupère la face du premier cube
        currentFace = ((Cube) board.getElement(0, size - 1)).getFace();

        j = 0;
        for (int i = 1; i < size; i++) {
            int compteur = 0;
            currentFace = ((Cube) board.getElement(i, j)).getFace();

            if (face == currentFace) {
                compteur++;
            }
            j++;

            diagonales.add(compteur);
        }
        alignements.add(lignes);
        alignements.add(colonnes);
        alignements.add(diagonales);

        System.out.println("alignements : " + alignements.toString());

        return alignements;

    }
}
