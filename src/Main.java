
/**
 * Jeu de NIM.
 *
 * @author Arthur Le Gall
 */
public class Main {

    public static void main(String[] args) {
        // Initialisation des variables
        int choixPartie;
        do {
            choixPartie = SimpleInput.getInt("1 : Joueur contre Joueur\n2 : Joueur contre Ordinateur\nChoisissez le mode de jeu -> ");
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
            // clearScreen();
            System.out.println("------ Mode de jeu : Joueur contre Ordinateur ------");

            int difficulty;
            do {
                difficulty = SimpleInput.getInt("Choisissez la difficulté de l'ordinateur (1 ou 2) -> ");
            } while (difficulty != 1 && difficulty != 2);
            String joueur1 = SimpleInput.getString("Entrez le nom du premier joueur: ");
            String joueur2 = "Ordinateur";
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
                jouer(joueur, allumettes, difficulty);

                if (verifWin(allumettes)) {
                    win = true;
                    System.out.println(joueur + " a gagné");
                }
                tour++;
            }
        }
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static int[] creerTableau(int nbLignes) {
        int[] allumettes = new int[nbLignes];
        int addAllumettes = 1;
        for (int i = 0; i < allumettes.length; i++) {
            allumettes[i] = addAllumettes;
            addAllumettes += 2;
        }
        return allumettes;
    }

    // Fonction d'un tour de jeu
    public static void jouer(String joueur, int[] allumettes, int level) {
        affichage(allumettes);
        int choixLigne;
        int nbAllumettes;
        System.out.println(joueur + " est en train de jouer.");
        if (!"Ordinateur".equals(joueur)) {
            do {
                choixLigne = SimpleInput.getInt("Quelle ligne souhaitez vous jouer ?");
            } while (!verifLigne(choixLigne, allumettes));
            do {
                nbAllumettes = SimpleInput.getInt("Combien d'allumettes souhaitez vous retirer ?");
            } while (!verifAllumettes(choixLigne, nbAllumettes, allumettes));
        } else {
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
        }

        allumettes[choixLigne - 1] -= nbAllumettes;
    }

    public static int[] ordiFacile(int[] allumettes) {
        int[] res = new int[2];
        int nbAllumettes;
        int choixLigne;
        do {
            choixLigne = (int) (Math.random() * allumettes.length) + 1;
        } while (allumettes[choixLigne - 1] == 0);
        System.out.println("L'ordinateur a choisi la ligne " + choixLigne);
        res[0] = choixLigne;

        // Select a valid number of sticks to remove (at least 1, at most the number of sticks in the line)
        nbAllumettes = (int) (Math.random() * allumettes[choixLigne - 1]) + 1;
        System.out.println("L'ordinateur a choisi de retirer " + nbAllumettes + " allumettes");
        res[1] = nbAllumettes;

        return res;
    }

    public static int[] ordiDifficile(int[] allumettes) {
        int[] res = new int[2];
        int binaryLength = Integer.toBinaryString(allumettes[allumettes.length - 1]).length();
        int[] resBinaries = new int[binaryLength];
        int[][] binaries = new int[allumettes.length][binaryLength];
        int nbZeros = 0;

        for (int i = 0; i < allumettes.length; i++) {
            String binary = Integer.toBinaryString(allumettes[i]);
            System.out.println("Binaire : " + binary);

            if (binary.length() < binaryLength) {
                nbZeros = binaryLength - binary.length();
                for (int j = 0; j < nbZeros; j++) {
                    binary = "0" + binary;
                }
            }
            for (int k = 0; k < binary.length(); k++) {
                if (binary.charAt(k) == '1') {
                    binaries[i][k] = 1;
                } else if (binary.charAt(k) == '0') {
                    binaries[i][k] = 0;
                }
            }
        }

        displayTab2(binaries);

        System.out.println(binaryLength);
        for (int i = 0; i < binaries.length; i++) {
            for (int j = 0; j < binaries[i].length; j++) {
                resBinaries[j] += binaries[i][j];
            }
        }
        
        displayTab(resBinaries);
        return res;
    }

    // Fonction d'affichage du tableau d'allumettes
    public static void affichage(int[] tab) {
        for (int i = 0; i < tab.length; i++) {
            System.out.print(i + 1 + " :");
            for (int j = 0; j < tab[i]; j++) {
                System.out.print(" |");
            }
            System.out.println("");
        }
    }

    public static void displayTab(int[] t) {
        int i = 0;
        System.out.print("{");
        while (i < t.length - 1) {
            System.out.print(t[i] + ",");
            i = i + 1;
        }
        System.out.println(t[i] + "}");
    }

    public static void displayTab2(int[][] t) {
        System.out.print("{");
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
        System.out.println("}");
    }

    // Fonction de verification de la valeur selectionnée
    public static boolean verifLigne(int ligne, int[] allumettes) {
        return (ligne <= allumettes.length && ligne >= 1 && allumettes[ligne - 1] > 0);
    }

    // Fonction de vériffication du nombre d'allumettes à enlever de la ligne
    public static boolean verifAllumettes(int ligne, int nbAllumettes, int[] allumettes) {
        return (allumettes[ligne - 1] >= nbAllumettes && nbAllumettes >= 1);
    }

    // Fonction de vérification de la victoire
    public static boolean verifWin(int[] tab) {
        boolean win = true;
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] != 0) {
                win = false;
            }
        }
        return win;
    }
}
