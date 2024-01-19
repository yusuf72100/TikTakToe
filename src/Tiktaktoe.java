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
            dispose();
        });

        setLocationRelativeTo(null);    // place la fenêtre au milieu de l'écran
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // évèement d'écoute sur la fenêtre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Server.stopServer();
                Client.stopClient();
                dispose(); // Ferme la fenêtre
            }
        });
    }   

    /**
     * Cette méthode initialisera l'affichage de la grille
     */
    private void drawGrid() {
        for (int i = 0; i < 9; i++) {
            cases[i] = new JButton((i + 1) + "");
            cases[i].setBounds(80 * (i % 3), 80 * (i / 3), 80, 80);
            add(cases[i]);

            // on affecte l'évènement du bouton
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
