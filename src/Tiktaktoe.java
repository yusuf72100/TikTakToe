import java.awt.event.*;
import javax.swing.*;

/**
 * Classe principale de mon programme
 */
public class Tiktaktoe extends JFrame{
    private final JButton Jouer;
    private final JButton Quitter;
    JButton[] cases;

    /** Ce constructeur initialisera la fenêtre */
    Tiktaktoe() {
        super("TikTakToe de la truite");
        setSize(600 , 600);

        cases = new JButton[9];
        Jouer = new JButton("Jouer-(placeholder)");
        Jouer.setBounds((getWidth()/2)-150,getHeight()/2-150,300,100);
        Quitter = new JButton("Quitter-(placeholder)");
        Quitter.setBounds((getWidth()/2)-150,getHeight()/2,300,100);

        add(Jouer);
        add(Quitter);
        
        setButtonsEventsHandlers(Jouer, () -> {
            remove(Jouer);
            remove(Quitter);
            repaint();
            drawGrid();
            Server.startServer();
        });
        setButtonsEventsHandlers(Quitter, () -> {
            Server.stopServer();
            Client.stopClient();
            dispose(); // Ferme la fenêtre
            System.exit(0);
        });

        setLocationRelativeTo(null);    // place la fenêtre au milieu de l'écran
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Évènement d'écoute sur la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Server.stopServer();
                Client.stopClient();
                dispose(); // Ferme la fenêtre
                System.exit(0);
            }
        });
    }   

    /** Cette méthode initialisera l'affichage de la grille */
    private void drawGrid() {
        int buttonSize = 80;
        int gap = 10; // Espacement entre les boutons
        int rows = 3;
        int columns = 3;

        // Calcul des dimensions de la grille
        int gridWidth = columns * (buttonSize + gap) - gap;
        int gridHeight = rows * (buttonSize + gap) - gap;

        // Calcul des coordonnées pour centrer la grille dans la fenêtre
        int startX = (getWidth() - gridWidth) / 2;
        int startY = (getHeight() - gridHeight) / 2;

        for (int i = 0; i < 9; i++) {
            cases[i] = new JButton((i + 1) + "");

            // Calcul des coordonnées pour chaque bouton
            int x = startX + (i % 3) * (buttonSize + gap);
            int y = startY + (i / 3) * (buttonSize + gap);

            cases[i].setBounds(x, y, buttonSize, buttonSize);
            add(cases[i]);

            // On affecte l'évènement du bouton
            int finalI = i;
            setButtonsEventsHandlers(this.cases[i], () -> {
                Client.sendData(finalI);
            });
        }
    }

    /** Cette méthode initialisera l'évènement du bouton voulut avec une action voulut */
    private void setButtonsEventsHandlers(JButton button, Runnable action) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    public static void main(String[] args){
        Tiktaktoe ttt = new Tiktaktoe();
        Grid grid = new Grid();
        Server socket = new Server();
    }
}
