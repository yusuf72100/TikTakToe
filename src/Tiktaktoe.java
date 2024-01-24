import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.*;

/**
 * Classe principale de mon programme
 */
public class Tiktaktoe extends JFrame{
    private final JButton Jouer;
    private final JButton Quitter;
    private final JButton Heberger;
    private final JButton Rejoindre;
    private final Font labelFont;
    private final JLabel label;
    private final JButton[] cases;
    private final JMenu MainMenu = new JMenu("Main");
    private static Client client;
    private static Server server;

    /** Ce constructeur initialisera la fenêtre */
    Tiktaktoe() {
        super("TikTakToe de la truite");
        setSize(600 , 600);

        labelFont = new Font("Arial", Font.BOLD, 20);
        label = new JLabel("En attente d'un joueur...");

        cases = new JButton[9];
        Jouer = new JButton("Jouer-(placeholder)");
        Jouer.setBounds((getWidth()/2)-150,getHeight()/2-150,300,100);
        Heberger = new JButton("Héberger-(placeholder)");
        Heberger.setBounds((getWidth()/2)-150,getHeight()/2-150,300,100);
        Quitter = new JButton("Quitter-(placeholder)");
        Quitter.setBounds((getWidth()/2)-150,getHeight()/2,300,100);
        Rejoindre = new JButton("Rejoindre-(placeholder)");
        Rejoindre.setBounds((getWidth()/2)-150,getHeight()/2,300,100);

        mainMenu();

        setButtonsEventsHandlers(Jouer, () -> {
            remove(Jouer);
            remove(Quitter);
            repaint();
            add(Heberger);
            add(Rejoindre);
        });

        setButtonsEventsHandlers(Heberger, () -> {
            waitingScreen();
            Server.startServer(this);
            client = new Client("localhost", this);
        });

        setButtonsEventsHandlers(Rejoindre, () -> {
            remove(Heberger);
            remove(Rejoindre);
            client = new Client("localhost", this);
            drawGrid();
            repaint();
        });

        setButtonsEventsHandlers(Quitter, () -> {
            Server.stopServer();
            client.stopClient();
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
                if (client != null) client.stopClient();
                dispose(); // Ferme la fenêtre
                System.exit(0);
            }
        });
    }

    private void mainMenu() {
        remove(Jouer);
        remove(Quitter);
        remove(label);
        add(Jouer);
        add(Quitter);
        repaint();
    }

    /** Cette méthode initialisera l'affichage de la grille */
    private void drawGrid() {
        int buttonSize = 80;
        int gap = 10;           // Espacement entre les boutons
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

            // On affecte l'évènement du bouton avec un lambda
            int finalI = i;
            setButtonsEventsHandlers(this.cases[i], () -> {
                if(client != null){
                    client.sendData(finalI);
                }
                else {
                    try {
                        client.server.sendDataToClient(finalI);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    /** Cette méthode initialisera l'évènement du bouton voulut avec une action voulue */
    private void setButtonsEventsHandlers(JButton button, Runnable action) {
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                action.run();
            }
        });
    }

    private void waitingScreen() {
        remove(Heberger);
        remove(Rejoindre);

        label.setFont(labelFont);
        label.setBounds((getWidth()-200)/2, ((getHeight()-100)/2)-20, 300 , 100);
        add(label);
        repaint();
    }

    public void startGame() {
        remove(label);
        drawGrid();
        repaint();
        Server.GameStarted = true;
    }

    public void stopGame(){

    }

    public static void main(String[] args){
        Tiktaktoe ttt = new Tiktaktoe();
        Grid grid = new Grid();
    }
}
