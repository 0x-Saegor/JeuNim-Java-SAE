/**
 * Jeu de NIM.
 * @author Arthur Le Gall
 */

public class Main {

    public static void main(String[] args) {
        // Initialisation des variables
        String joueur1 = SimpleInput.getString("Entrez le nom du premier joueur: ");
        String joueur2 = SimpleInput.getString("Entrez le nom du deuxième joueur: ");
        String[] joueurs = {joueur1, joueur2};
        int nombreLignes;
        boolean win = false;
        int tour = 0;
        int addAllumettes = 1;
        do {
            nombreLignes = SimpleInput.getInt("Combien de lignes d'allumettes souhaitez vous ? -> ");
        } while (nombreLignes < 2);

        // Création du tableau d'allumettes
        int[] allumettes = new int[nombreLignes];
        for (int i = 0; i < allumettes.length; i++) {
            allumettes[i] = addAllumettes;
            addAllumettes += 2;
        }

        // Boucle principale
        while (!win) {
            String joueur = joueurs[tour % 2];
            jouer(joueur, allumettes);

            if (verifWin(allumettes)) {
                win = true;
                System.out.println(joueur + " a gagné");
            }
            tour++;
        }
    }

    // Fonction d'un tour de jeu
    public static void jouer(String joueur, int[] allumettes) {
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
