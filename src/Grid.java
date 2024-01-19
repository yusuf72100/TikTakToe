import java.util.*;

public class Grid extends Server {
    private List<List<Integer>> grid;

    /**
     * Ce constructeur initialisera la grille
     */
    Grid () {
        this.grid = Arrays.asList(
            Arrays.asList(0, 0, 0),
            Arrays.asList(0, 0, 0),
            Arrays.asList(0, 0, 0)
        );
    }

    /**
     * Cette méthode permettra de placer une signature à une certaine postition de la grille
     * @param x
     * @param y
     * @param signature
     */
    public void changeValue (int x, int y, int signature) {
        this.grid.get(x).set(y, signature);
    }

    /**
     * Nous renvoi la valeur à une position donnée
     * @param x
     * @param y
     * @return integer
     */
    public int getValue(int x, int y) {
        return this.grid.get(x).get(y);
    }
}
