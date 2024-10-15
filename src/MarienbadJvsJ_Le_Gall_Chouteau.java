
/**
 * Jeu de NIM.
 *
 * La classe MarienbadJvsJ_Le_Gall_Chouteau contient la méthode principale qui sert de point d'entrée pour l'application.
 * Elle propose un mode de jeu Joueur contre Joueur.
 *
 * La classe contient également diverses méthodes auxiliaires pour faciliter la logique du jeu, notamment :
 * - clearScreen : Efface l'écran de la console.
 * - creerTableau : Crée le plateau de jeu initial avec un nombre spécifié de lignes.
 * - jouer : Gère le déroulement du jeu pour chaque tour.
 * - affichage : Affiche l'état actuel du plateau de jeu.
 * - displayTab : Affiche un tableau avec des retours à la ligne.
 * - displayTab_TestCas : Affiche un tableau sans retours à la ligne.
 * - verifLigne : Vérifie si la ligne sélectionnée est valide.
 * - verifAllumettes : Vérifie si le nombre d'allumettes à retirer est valide.
 * - verifWin : Vérifie si le jeu est gagné.
 *
 * La méthode principale inclut également des cas de test pour les méthodes auxiliaires afin d'assurer leur exactitude.
 *
 * @author Arthur Le Gall, Lydéric Chouteau
 */
public class MarienbadJvsJ_Le_Gall_Chouteau {

    public static void main(String[] args) {
        // Initialisation des variables
        int choixPartie;
        do {
            choixPartie = SimpleInput.getInt("1 : Joueur contre Joueur\n2 : Méthodes de test\nChoisissez une option -> ");
        } while (choixPartie != 1 && choixPartie != 2);

        if (choixPartie == 1) {
            clearScreen();
            System.out.println("------ Mode de jeu : Joueur contre Joueur ------");

            String joueur1 = SimpleInput.getString("Entrez le nom du premier joueur: ");
            String joueur2 = SimpleInput.getString("Entrez le nom du deuxième joueur: ");
            String[] joueurs = {joueur1, joueur2};
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
                jouer(joueur, allumettes, 0);

                if (verifWin(allumettes)) {
                    win = true;
                    System.out.println(joueur + " a gagné");
                }
                tour++;
            }
        } else if (choixPartie == 2) {
            System.out.println("------ Mode de test ------");

            testVerifWin();
            System.out.println("----- Test de la méthode verifWin() terminé -----");

            testVerifAllumettes();
            System.out.println("----- Test de la méthode verifAllumettes() terminé -----");

            testVerifLigne();
            System.out.println("----- Test de la méthode verifLigne() terminé -----");

        }
    }

     /**
     * Efface tout l'affichage
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Créer un tableau de jeu
     *
     * @param nbLignes nombre de lignes du tableau
     * @return tab tableau de jeu
     */
    public static int[] creerTableau(int nbLignes) {
        int[] tab = new int[nbLignes];
        int addAllumettes = 1;
        for (int i = 0; i < tab.length; i++) {
            tab[i] = addAllumettes;
            addAllumettes += 2;
        }
        return tab;
    }

    /**
     * Un tour de jeu
     */
    public static void jouer(String joueur, int[] allumettes, int level) {
        clearScreen();
        affichage(allumettes);
        int choixLigne;
        int nbAllumettes;
        System.out.println(joueur + " est en train de jouer.");
        do {
            choixLigne = SimpleInput.getInt("Quelle ligne souhaitez vous jouer ?");
        } while (!verifLigne(choixLigne, allumettes));
        do {
            nbAllumettes = SimpleInput.getInt("Combien d'allumettes souhaitez vous retirer ?");
        } while (!verifAllumettes(choixLigne, nbAllumettes, allumettes));

        allumettes[choixLigne - 1] -= nbAllumettes;
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
     * @param tab : tableau d'allumettes
     * @param ligne : ligne sélectionnée
     * @param result : resultat attendu
     */
    public static void testCasVerifLigne(int[] tab, int ligne, boolean result) {
        // Affichage
        System.out.print("verifLigne(");
        displayTab_TestCas(tab);
        System.out.print(", " + ligne + ") \t= " + result + "\t : ");

        // Appel
        boolean resExec = verifLigne(ligne, tab);
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
     * @param tab : tableau d'allumettes
     * @param ligne : ligne sélectionnée
     * @param nbAllumettes : nombre d'allumettes à enlever
     * @param result : resultat attendu
     */
    public static void testCasVerifAllumettes(int[] tab, int ligne, int nbAllumettes, boolean result) {
        // Affichage
        System.out.print("verifAllumettes(");
        displayTab_TestCas(tab);
        System.out.print(", " + ligne + ", " + nbAllumettes + ") \t= " + result + "\t : ");

        // Appel
        boolean resExec = verifAllumettes(ligne, nbAllumettes, tab);
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
    public static boolean verifWin(int[] tab) {
        boolean win = true;
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] != 0) {
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
}
