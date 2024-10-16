
/**
 * Jeu de NIM.
 *
 * La classe MarienbadJvsO_Le_Gall_Chouteau contient la méthode principale qui sert de point d'entrée pour l'application.
 * Elle propose un menu permettant à l'utilisateur de choisir entre différents modes de jeu :
 * 1. Joueur contre Ordinateur
 * 2. Méthodes de test
 *
 * En fonction du choix de l'utilisateur, le mode de jeu approprié est lancé.
 *
 * La classe contient également diverses méthodes auxiliaires pour faciliter la logique du jeu, notamment :
 * - clearScreen : Efface l'écran de la console.
 * - creerTableau : Crée le plateau de jeu initial avec un nombre spécifié de lignes.
 * - jouer : Gère le déroulement du jeu pour chaque tour.
 * - ordiFacile : Gère le coup de l'ordinateur en mode facile.
 * - ordiDifficile : Gère le coup de l'ordinateur en mode difficile.
 * - affichage : Affiche l'état actuel du plateau de jeu.
 * - displayTab : Affiche un tableau avec des retours à la ligne.
 * - displayTab_TestCas : Affiche un tableau sans retours à la ligne.
 * - displayTabDouble : Affiche un tableau 2D.
 * - verifLigne : Vérifie si la ligne sélectionnée est valide.
 * - verifAllumettes : Vérifie si le nombre d'allumettes à retirer est valide.
 * - verifWin : Vérifie si le jeu est gagné.
 * - testVerifAllumettes : Teste la méthode verifAllumettes.
 * - testCasVerifAllumettes : Teste un cas spécifique pour verifAllumettes.
 * - testVerifWin : Teste la méthode verifWin.
 * - testCasVerifWin : Teste un cas spécifique pour verifWin.
 * - calcNimSum : Calcule le NimSum pour le plateau de jeu.
 * - testCalcNimSum : Teste la méthode calcNimSum.
 * - testCasCalcNimSum : Teste un cas spécifique pour calcNimSum.
 *
 * La méthode principale inclut également des cas de test pour les méthodes auxiliaires afin d'assurer leur exactitude.
 *
 * @author Arthur Le Gall & Lydéric Chouteau
 */
public class MarienbadJvsO_Le_Gall_Chouteau {

    public static void main(String[] args) {
        // Initialisation des variables
        int choixPartie;
        do {
            choixPartie = SimpleInput.getInt("1 : Joueur contre Ordinateur\n2 : Methodes de test\nChoisissez le mode de jeu -> ");
        } while (choixPartie != 1 && choixPartie != 2);

        if (choixPartie == 1) {
            // clearScreen();
            System.out.println("------ Mode de jeu : Joueur contre Ordinateur ------");
            int difficulty;

            do {
                difficulty = SimpleInput.getInt("Choisissez la difficulte de l'ordinateur (1 ou 2) -> ");
            } while (difficulty != 1 && difficulty != 2);

            clearScreen();

            String joueur1 = SimpleInput.getString("Entrez le nom du joueur: ");
            String joueur2 = "Ordinateur";
            String[] joueurs = new String[2];

            int premierJoueur;
            do {
                premierJoueur = SimpleInput.getInt("Premier Joueur? (1 = Vous, 2 = Ordinateur): ");
            } while (premierJoueur != 1 && premierJoueur != 2);
            if (premierJoueur == 1) {
                joueurs[0] = joueur1;
                joueurs[1] = joueur2;
            } else {
                joueurs[0] = joueur2;
                joueurs[1] = joueur1;
            }
            int nombreLignes;
            boolean win = false;
            int tour = 0;
            do {
                nombreLignes = SimpleInput.getInt("Combien de lignes d'allumettes souhaitez vous ? -> ");
            } while (nombreLignes < 2);

            // Création du tableau d'allumettes
            int[] allumettes = creerTableau(nombreLignes);

            // Boucle principale
            while (!win) {
                String joueur = joueurs[tour % 2];
                jouer(joueur, allumettes, difficulty);

                if (verifWin(allumettes)) {
                    win = true;
                    System.out.println(joueur + " a gagne");
                }
                tour++;
            }
        } else if (choixPartie == 2) {
            System.out.println("------ Mode de test ------");

            testCalcNimSum();
            System.out.println("----- Test de la methode calcNimSum() termine -----");

            testVerifWin();
            System.out.println("----- Test de la methode verifWin() termine -----");

            testVerifAllumettes();
            System.out.println("----- Test de la methode verifAllumettes() termine -----");

            testVerifLigne();
            System.out.println("----- Test de la methode verifLigne() termine -----");

        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Créer un tableau de jeu
     *
     * @return tabs : tableau de jeu
     */
    public static int[] creerTableau(int nbLignes) {
        int[] tabs = new int[nbLignes];
        int addAllumettes = 1;
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = addAllumettes;
            addAllumettes += 2;
        }
        return tabs;
    }

    /**
     * Un tour de jeu
     */
    public static void jouer(String joueur, int[] allumettes, int level) {
        affichage(allumettes);
        int choixLigne;
        int nbAllumettes;
        System.out.println(joueur + " est en train de jouer.");
        if ("Ordinateur".equals(joueur)) {
            clearScreen();
            // Mode facile level = 1
            int[] res;
            if (level == 1) {
                res = ordiFacile(allumettes);
                choixLigne = res[0];
                nbAllumettes = res[1];
            } else {
                // Mode difficile level = 2
                res = ordiDifficile(allumettes);
                choixLigne = res[0];
                nbAllumettes = res[1];
            }

        } else {
            do {
                choixLigne = SimpleInput.getInt("Quelle ligne souhaitez vous jouer ? ");
            } while (!verifLigne(choixLigne, allumettes));
            do {
                nbAllumettes = SimpleInput.getInt("Combien d'allumettes souhaitez vous retirer ? ");
            } while (!verifAllumettes(choixLigne, nbAllumettes, allumettes));
        }

        allumettes[choixLigne - 1] -= nbAllumettes;
    }

    /**
     * Fait jouer l'ordinateur en mode facile
     *
     * @param tab tableau de jeu
     * @return res tableau contenant la ligne à jouer et les nombre d'allumette
     * à enlever
     */
    public static int[] ordiFacile(int[] tab) {
        int[] res = new int[2];
        int nbAllumettes;
        int choixLigne;
        do {
            choixLigne = (int) (Math.random() * tab.length) + 1;
        } while (tab[choixLigne - 1] == 0);
        System.out.println("L'ordinateur a choisi la ligne " + choixLigne);
        res[0] = choixLigne;

        // Selectionne un nombre valide d'allumettes à enlever (au minimum 1, au maximum le nombre d'allumettes sur la ligne)
        nbAllumettes = (int) (Math.random() * tab[choixLigne - 1]) + 1;
        System.out.println("L'ordinateur a choisi de retirer " + nbAllumettes + " allumettes");
        res[1] = nbAllumettes;

        return res;
    }

    /**
     * Fait jouer l'ordinateur en mode difficile
     *
     * @param tab tableau de jeu
     * @return res tableau contenant la ligne à jouer et les nombre d'allumettes
     * à enlever
     *
     */
    public static int[] ordiDifficile(int[] tab) {
        int[] res = new int[2];
        // Calculer le NimSum pour déterminer si l'ordinateur peut forcer une victoire ou non.
        int nimSum = calcNimSum(tab);

        if (nimSum != 0) {
            // Trouver un coup qui provoque Nim-Sum == zéro. Si nimsum = 0, l'ordinateur ne peut pas forcer une victoire, alors il joue aléatoirement.
            for (int i = 0; i < tab.length; i++) {
                int ligne = tab[i];
                int targetLigne = ligne ^ nimSum;  // Nombre d'allumettes que l'on veut sur la ligne après avoir joué.

                if (targetLigne < ligne) {
                    res[0] = i + 1;  // Stocker le numéro de la ligne (index basé sur 1)
                    res[1] = ligne - targetLigne;  // Nombre d'allumettes à retirer pour atteindre targetLigne
                    System.out.println("L'ordinateur a choisi la ligne " + res[0]);
                    System.out.println("L'ordinateur a choisi de retirer " + res[1] + " allumettes");
                }
            }
        }

        // Si l'ordinateur n'a trouvé aucune solution ou si NimSum == 0, il joue aléatoirement.
        if (res[0] == 0 && res[1] == 0) {
            res = ordiFacile(tab);
        }
        return res;
    }

    /**
     * Fonction d'affichage du tableau d'allumettes
     */
    public static void affichage(int[] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + 1 + " :");
            for (int j = 0; j < tab[i]; j++) {
                System.out.print(" |");
            }
            System.out.println("");
        }
    }

    /**
     * Affiche un tableau en revenant à la ligne
     */
    public static void displayTab(int[] t) {
        int i = 0;
        System.out.print("{");
        if (t.length > 0) {
            while (i < t.length - 1) {
                System.out.print(t[i] + ",");
                i = i + 1;
            }
        }
        System.out.println(t[i] + "}");
    }

    /**
     * Affiche un tableau sans revenir à la ligne
     */
    public static void displayTab_TestCas(int[] t) {
        int i = 0;
        System.out.print("{");
        if (t.length > 0) {
            while (i < t.length - 1) {
                System.out.print(t[i] + ",");
                i = i + 1;
            }
            System.out.print(t[i] + "}");
        } else {
            System.out.print("}");
        }

    }

    /**
     * Affichage d'un tableau de tableau
     */
    public static void displayTabDouble(int[][] t) {
        System.out.print("{");
        if (t.length > 0) {
            for (int i = 0; i < t.length; i++) {
                System.out.print("{");
                for (int j = 0; j < t[i].length; j++) {
                    System.out.print(t[i][j]);
                    if (j < t[i].length - 1) {
                        System.out.print(",");
                    }
                }
                System.out.print("}");
                if (i < t.length - 1) {
                    System.out.print(",");
                }
            }
        }
        System.out.println("}");
    }

    /**
     * Fonction de verification de la ligne selectionnée
     *
     * @param ligne numero de la ligne selectionnée
     * @param nbAllumettes nombre souhaité d'allumettes à enlever
     * @param tab tableau de jeu
     * @return nimsum valeur de la NimSum
     */
    public static boolean verifLigne(int ligne, int[] tab) {
        return (ligne > 0 && ligne <= tab.length && tab[ligne - 1] > 0);
    }

    /**
     * Teste la méthode verifLigne()
     */
    public static void testVerifLigne() {
        System.out.println();
        System.out.println("*** testVerifLigne()");
        testCasVerifLigne(new int[]{3, 5, 7}, 1, true);
        testCasVerifLigne(new int[]{3, 5, 7}, 2, true);
        testCasVerifLigne(new int[]{3, 5, 7}, 4, false);
        testCasVerifLigne(new int[]{0, 0, 0}, 1, false);
        testCasVerifLigne(new int[]{1, 0, 0}, 1, true);
    }

    /**
     * teste un appel de verifLigne
     *
     * @param allumettes : tableau d'allumettes
     * @param ligne : ligne sélectionnée
     * @param result : resultat attendu
     */
    public static void testCasVerifLigne(int[] allumettes, int ligne, boolean result) {
        // Affichage
        System.out.print("verifLigne(");
        displayTab_TestCas(allumettes);
        System.out.print(", " + ligne + ") \t= " + result + "\t : ");

        // Appel
        boolean resExec = verifLigne(ligne, allumettes);
        // Verification
        if (resExec == result) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR");
        }
    }

    /**
     * Fonction de vériffication du nombre d'allumettes à enlever de la ligne
     *
     * @param ligne numero de la ligne selectionnée
     * @param nbAllumettes nombre souhaité d'allumettes à enlever
     * @param tab tableau de jeu
     * @return nimsum valeur de la NimSum
     */
    public static boolean verifAllumettes(int ligne, int nbAllumettes, int[] tab) {
        return (tab[ligne - 1] >= nbAllumettes && nbAllumettes >= 1);
    }

    /**
     * Teste la méthode verifAllumettes()
     */
    public static void testVerifAllumettes() {
        System.out.println();
        System.out.println("*** testVerifAllumettes()");
        testCasVerifAllumettes(new int[]{3, 5, 7}, 1, 3, true);
        testCasVerifAllumettes(new int[]{3, 5, 7}, 2, 6, false);
        testCasVerifAllumettes(new int[]{3, 5, 7}, 3, 0, false);
        testCasVerifAllumettes(new int[]{3, 5, 7}, 3, 7, true);
        testCasVerifAllumettes(new int[]{3, 5, 7}, 3, 8, false);
    }

    /**
     * teste un appel de verifAllumettes
     *
     * @param allumettes : tableau d'allumettes
     * @param ligne : ligne sélectionnée
     * @param nbAllumettes : nombre d'allumettes à enlever
     * @param result : resultat attendu
     */
    public static void testCasVerifAllumettes(int[] allumettes, int ligne, int nbAllumettes, boolean result) {
        // Affichage
        System.out.print("verifAllumettes(");
        displayTab_TestCas(allumettes);
        System.out.print(", " + ligne + ", " + nbAllumettes + ") \t= " + result + "\t : ");

        // Appel
        boolean resExec = verifAllumettes(ligne, nbAllumettes, allumettes);
        // Verification
        if (resExec == result) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR");
        }
    }

    /**
     * Verifie la condition de victoire
     *
     * @param tab le plateau de jeu
     * @return win vaut true si toutes les lignes sont vides
     */
    public static boolean verifWin(int[] allumettes) {
        boolean win = true;
        for (int i = 0; i < allumettes.length; i++) {
            if (allumettes[i] != 0) {
                win = false;
            }
        }
        return win;
    }

    /**
     * Teste la méthode verifWin()
     */
    public static void testVerifWin() {
        System.out.println();
        System.out.println("*** testVerifWin()");
        testCasVerifWin(new int[]{0, 0, 0}, true);
        testCasVerifWin(new int[]{1, 0, 0}, false);
        testCasVerifWin(new int[]{0, 1, 0}, false);
        testCasVerifWin(new int[]{0, 0, 1}, false);
        testCasVerifWin(new int[]{1, 1, 1}, false);
        testCasVerifWin(new int[]{0}, true);
        testCasVerifWin(new int[]{1}, false);
    }

    /**
     * teste un appel de verifWin
     *
     * @param allumettes : tableau d'allumettes
     * @param result : resultat attendu
     */
    public static void testCasVerifWin(int[] allumettes, boolean result) {
        // Affichage
        System.out.print("verifWin(");
        displayTab_TestCas(allumettes);
        System.out.print(") \t= " + result + "\t : ");

        // Appel
        boolean resExec = verifWin(allumettes);
        // Verification
        if (resExec == result) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR");
        }
    }

    /**
     * Calcul de la NimSum (permet de calculer le nombre d'allumette qu'enleve
     * l'ordinateur)
     *
     * @param tab le plateau de jeu
     * @return nimsum valeur de la NimSum
     */
    static int calcNimSum(int[] tab) {
        int i;
        int nimsum = 0;
        if (tab.length > 0) {
            nimsum = tab[0];
            for (i = 1; i < tab.length; i++) {
                nimsum = nimsum ^ tab[i];
            }
        }
        return (nimsum);
    }

    /**
     * Teste la méthode calcNimSum()
     */
    public static void testCalcNimSum() {
        System.out.println();
        System.out.println("*** testCalcNimSum()");
        testCasCalcNimSum(new int[]{1, 3, 5}, 7);
        testCasCalcNimSum(new int[]{4}, 4);
        testCasCalcNimSum(new int[]{1, 2}, 3);
        testCasCalcNimSum(new int[]{}, 0);
        testCasCalcNimSum(new int[]{1, 1, 1, 1}, 0);
        testCasCalcNimSum(new int[]{1024, 512}, 1536);

    }

    /**
     * teste un appel de calcNimSum
     *
     * @param allumettes : tableau d'allumettes
     * @param result : resultat attendu
     */
    public static void testCasCalcNimSum(int[] allumettes, int result) {
        // Affichage
        System.out.print("calcNimSum(");
        displayTab_TestCas(allumettes);
        System.out.print(") \t= " + result + "\t : ");

        // Appel
        int resExec = calcNimSum(allumettes);
        // Verification
        if (resExec == result) {
            System.out.println("OK");
        } else {
            System.err.println("ERREUR");
        }
    }

}
