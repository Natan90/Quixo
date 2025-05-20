package control;

import boardifier.control.ActionFactory;
import boardifier.control.Controller;
import boardifier.control.Decider;
import boardifier.model.ContainerElement;
import boardifier.model.Model;
import boardifier.model.action.ActionList;
import model.Cube;
import model.QuixoBoard;
import model.QuixoStageModel;

import java.awt.*;
import java.util.*;
import java.util.List;

public class QuixoDecider extends Decider {

    private static final Random loto = new Random(Calendar.getInstance().getTimeInMillis());

    public QuixoDecider(Model model, Controller control) {
        super(model, control);
    }

    int max;
    QuixoBoard board;


    @Override
    public ActionList decide() {
        // do a cast get a variable of the real type to get access to the attributes of HoleStageModel
        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();
        board = stage.getBoard(); // get the board
        ContainerElement pot = null; // the pot where to take a pawn
        pot = stage.getRedPot();

        QuixoController quixoController = (QuixoController) control;



        int[] coordCube = new int[2];

        // Liste des coins. Si aucun coup n'est interessant à jouer, le bot jouera dans les coins
        List<Point> coins = List.of(new Point(0, 0), new Point(0, 4), new Point(4, 0), new Point(4, 4));
        System.out.println("coins : " + coins.toString());

        List<Point> valid = board.computeValidCells(true, coordCube, model);
        System.out.println(valid.toString());
        //regarder si on ne peut pas aligner 5 cubes
        // Si non, essayer de creer la plus grande serie de cube possible
        // Si il n'y a pas de possibilité de faire une ligne de 3,

        int[] move = play();

        int cubeRow = move[0];
        int cubeCol = move[1];
        int destRow = move[2];
        int destCol = move[3];
        System.out.println("cubeRow : " + cubeRow + ", cubeCol : " + cubeCol + ", destRow : " + destRow + ", destCol : " + destCol);

        if (cubeRow == 0 && cubeCol == 0 && destRow == 0 && destCol == 0) {
            // Pas de coup trouvé : jouer un coup par défaut, par exemple prendre un coin
            Cube defaultCube = (Cube) board.getElement(0, 0);
            ActionList defaultAction = ActionFactory.generatePutInContainer(model, defaultCube, "cubepot", 0, 0);
            defaultAction.setDoEndOfTurn(true);
            return defaultAction;
        }
        ActionList actions = new ActionList();

        Cube cube = (Cube) pot.getElement(0, 0);

        if (quixoController == null) {
            System.out.println("quixoController est null !");
        } else {
            quixoController.mooveSequenceCube(destRow, destCol, cubeRow, cubeCol, false);
            System.out.println("quixoController n'est pas null.");
        }

        if (cube != null) {
            actions.addAll(firstTurn(cubeRow, cubeCol, destRow, destCol));
            actions.addAll(ActionFactory.generatePutInContainer(model, cube, "quixoboard", destRow, destCol));
        }

        return actions;
    }

    public ActionList firstTurn(int cubeRow, int cubeCol, int destRow, int destCol) {
        System.out.println("cubeRow : " + cubeRow + ", cubeCol : " + cubeCol + ", destRow : " + destRow + ", destCol : " + destCol);

        Cube cube = (Cube) board.getElement(cubeRow, cubeCol);

        ActionList actions = new ActionList();

        if (cube != null && cube.getFace() != model.getCurrentPlayer().getType()) {
            cube.setFace(2);
            actions.addAll(ActionFactory.generatePutInContainer(model, cube, "cubepot", 0, 0));
            actions.setDoEndOfTurn(true); // after playing this action list, it will be the end of turn for current player.
        }

        return actions;
    }


    // Renvoie les coordonnées du cube choisi et les coordonnées de la destination de ce cube (1er coup / 2e coup)
    public int[] play() {
        QuixoStageModel stage = (QuixoStageModel) model.getGameStage();
        board = stage.getBoard();

        int size = 5;

        // Récupérer dans un premier temps les meilleures lignes
        List<List<Integer>> alignementsOpponent = new ArrayList<>(getAlignement(1));
        List<List<Integer>> alignementsCurrentPlayer = new ArrayList<>(getAlignement(2));
        System.out.println(alignementsOpponent);
        System.out.println(alignementsCurrentPlayer);


        // retourne les coordonnées dans la liste des scores d'alignements les plus grands
        List<Point> coordMaxOpponent = new ArrayList<>(searchMax(alignementsOpponent));

        System.out.println("coordMaxOpponent : " + coordMaxOpponent);
        System.out.println("max : " + max);
        List<Point> coordMaxCurrentPlayer = new ArrayList<>(searchMax(alignementsCurrentPlayer));
        System.out.println("coordMaxCurrentPlayer : " + coordMaxCurrentPlayer);

        List<Point> bestCasesToWin = getBestCubes(coordMaxCurrentPlayer);  // pour gagner
        // retourne les coordonnées des cases manquantes pour compléter la ligne de 5
        List<Point> bestCasesToBlock = getBestCubes(coordMaxOpponent);     // pour bloquer
        System.out.println("bestCasesToWin : " + bestCasesToWin);
        System.out.println("bestCasesToBlock : " + bestCasesToBlock);

        List<Point> firstMoves = board.computeValidCells(true, new int[2], model);

        if (max > 2) {
            // On essaye d'abord de blocker l'adversaire -> stratégie défensive
            for (int i = 0; i < bestCasesToBlock.size(); i++) {
                Point cible = bestCasesToBlock.get(i);
                int[] move = trouverSecondMove(cible, firstMoves);
                System.out.println("move bestCasesToBlock : "+ Arrays.toString(move));
                if (move != null) {
                    return move;
                }
            }
        }

        // Ensuite on essaye de se faire gagner
        for (int i = 0; i < bestCasesToWin.size(); i++) {
            Point cible = bestCasesToWin.get(i);
            int[] move = trouverSecondMove(cible, firstMoves);

            if (move != null) {
                System.out.println("move bestCasesToWin : "+ Arrays.toString(move));
                return move;
            }
        }
        // Sinon on joue par défault
        return new int[]{0, 0, 0, 0};
    }

    // j'avais pas vu que tu avais fait la methode getCoordPremierCoup
    // je laisse les deux quand même
    public int[] trouverSecondMove(Point cible, List<Point> firstMove) {
        int[] coordCube = new int[2];

        for (int i = 0; i < firstMove.size(); i++) {
            Point posCube = firstMove.get(i);
            coordCube[0] = posCube.y;
            coordCube[1] = posCube.x;

            Cube cube = (Cube) board.getElement(coordCube[0], coordCube[1]);
            if (cube != null && cube.getFace() != model.getCurrentPlayer().getType() - 1) {
                List<Point> moves = board.computeValidCells(false, coordCube, model);
                for (int j = 0; j < moves.size(); j++) {
                    Point dest = moves.get(j);
                    if (dest.y == cible.y && dest.x == cible.x) {
                        return new int[]{coordCube[0], coordCube[1], dest.y, dest.x};
                    }
                }
            }


        }
        return null;
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
                Cube cube = (Cube) board.getElement(i, j);
                if (cube == null)
                    continue;

                currentFace = cube.getFace();
                if (face == currentFace)
                    compteur++;

            }
            lignes.add(compteur);

            // Vérification des colonnes
            compteur = 0;
            for (int j = 0; j < size; j++) {
                Cube cube = (Cube) board.getElement(j, i);
                if (cube == null)
                    continue;

                currentFace = cube.getFace();

                if (face == currentFace)
                    compteur++;
            }
            colonnes.add(compteur);
        }

        // Diagonale principale de (0,0) à (4,4)
        int compteur = 0;
        for (int i = 0; i < size; i++) {
            Cube cube = (Cube) board.getElement(i, i);
            if (cube == null)
                continue;

            currentFace = cube.getFace();
            if (face == currentFace) {
                compteur++;
            }
            if (i == size - 1)
                diagonales.add(compteur);
        }

        // Diagonale secondaire de (0,4) à (4,0)
        compteur = 0;
        for (int i = 0; i < size; i++) {
            Cube cube = (Cube) board.getElement(i, size - 1 - i);
            if (cube == null)
                continue;

            currentFace = cube.getFace();
            if (face == currentFace) {
                compteur++;
            }

            if (i == size - 1)
                diagonales.add(compteur);
        }

        alignements.add(lignes);
        alignements.add(colonnes);
        alignements.add(diagonales);

        return alignements;

    }

    public List<Point> searchMax(List<List<Integer>> liste) {
        List<Point> coordMax = new ArrayList<>();
        max = 0;

        for (int i = 0; i < liste.size(); i++) {
            for (int j = 0; j < liste.get(i).size(); j++) {
                int elem = liste.get(i).get(j);

                if (elem > max) {
                    max = elem;
                    coordMax.clear();
                    coordMax.add(new Point(i, j));
                } else if (elem == max) {
                    coordMax.add(new Point(i, j));
                }

            }
        }
        return coordMax;
    }

    // les param rentrés sont les coordonnées du coup interessant (donc le deuxieme coup, la finalité)
    // ce qu'on veut faire c'est de renvoyer la liste des coordonées du premier coup
    public List<Point> getCoordPremierCoup(int x, int y) {
        List<int[][]> coordPremierCoup = new ArrayList<>();
        int newX = 0, newY = 0;
        int[][] coord = new int[1][1];

        List<Point> valid = board.computeValidCells(false, new int[]{x, y}, model);


        return valid;
    }

    // méthode qui retourne les coordonnées des positions pour compléter les lignes de 5
    public List<Point> getBestCubes(List<Point> coordMaxInList) {
        List<Point> caseObjectif = new ArrayList<>();
        for (int i = 0; i < coordMaxInList.size(); i++) {
            int x = (int) coordMaxInList.get(i).getX();
            int y = (int) coordMaxInList.get(i).getY();

            switch (x){
                //Pour vérifier les lignes
                case 0:
                    for (int j = 0; j < 5; j++) {
//                        System.out.println("dans ligne");
                        cubeDetection(y, j, caseObjectif);
                    }
                    continue;

                //Pour vérifier les colonnes
                case 1:
//                     System.out.println("dans colonnes");
                         for (int j = 0; j < 5; j++) {
                             cubeDetection(j, y, caseObjectif);
                         }
                    continue;

                //Pour vérifier les diagos
                case 2:
                    if (y == 0) {
//                        System.out.println("dans diagonales1");
                        for (int j = 0; j < 5; j++) {
                            cubeDetection(j, j, caseObjectif);
                        }
                    } else {
//                        System.out.println("dans diagonales2");
                        for (int j = 0; j < 5; j++) {
                            cubeDetection(5 - 1 - j, j, caseObjectif);
                       }
                  }
             }
        }
        return caseObjectif;
    }


    private void cubeDetection(int i, int j, List<Point> caseObjectif) {
        Cube cube = (Cube) board.getElement(i, j);
        int currentFace = 0;
        if (cube != null)
            currentFace = cube.getFace();

        if (currentFace == 0 || currentFace != model.getCurrentPlayer().getType() - 1) {
            caseObjectif.add(new Point(j, i));
        }
    }


    public List<Point> getCasesManquantesLigne(int ligne, List<Integer> colonnesOccupees) {
        List<Point> casesAJouer = new ArrayList<>();

        for (int col = 0; col < 5; col++) {
            if (!colonnesOccupees.contains(col)) {
                casesAJouer.add(new Point(ligne, col));
            }
        }

        return casesAJouer;
    }
}
