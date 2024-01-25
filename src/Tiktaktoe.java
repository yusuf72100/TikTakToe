import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Objects;
import javax.swing.*;

/**
 * Classe principale de mon programme
 */
public class Tiktaktoe extends JFrame{
    private final JButton Jouer;
    private final JButton Quitter;
    private final JButton Heberger;
    private final JButton Rejoindre;
    private final JButton Restart;
    private final JButton RTH;
    private final Font labelFont;
    private final JLabel label;
    private static JButton[] cases;
    private final JMenu MainMenu = new JMenu("Main");
    private static Client client;
    private static Server server;
    public static boolean won;
    private static int turn;

    /** Ce constructeur initialisera la fenêtre */
    Tiktaktoe() {
        super("TikTakToe de la truite");
        setSize(600 , 600);
        turn = 1;
        won = false;

        Restart = new JButton("Rejouer");
        Restart.setBounds(10,50,100,25);
        RTH = new JButton("Quitter");
        RTH.setBounds(10,110,100,25);

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
        });

        setButtonsEventsHandlers(Restart, () -> {
            if(client != null) {
                client.sendData(500);
                try {
                    client.client.addVote();

                    if(client.client.getVotes() >= 2) {
                        client.client.setVotes(0);
                        client.server.setVotes(0);
                        restartGame();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                try {
                    Server.posServer.sendDataToClient(500);
                    Server.posServer.addVote();

                    if (Server.posServer.getVotes() >= 2) {
                        Server.posServer.setVotes(0);
                        restartGame();
                    }
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            remove(Restart);
            repaint();
        });

        setButtonsEventsHandlers(RTH, () -> {
            removeGrid();
            mainMenu();
            if(client != null) {
                try {
                    client.sendData(100);
                    client.stopClient();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                stopGame();
            }
        });

        setButtonsEventsHandlers(Rejoindre, () -> {
            remove(Heberger);
            remove(Rejoindre);
            try {
                client = new Client("localhost", this);
                client.server.registerClient(client.client);
                client.sendData(99);
            } catch (RemoteException | MalformedURLException | NotBoundException e) {
                throw new RuntimeException(e);
            }

            startGame();
            repaint();
        });

        setButtonsEventsHandlers(Quitter, () -> {
            Server.stopServer();
            try {
                client.stopClient();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
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
                if (client != null) {
                    try {
                        client.stopClient();
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                dispose(); // Ferme la fenêtre
                System.exit(0);
            }
        });
    }

    private void mainMenu() {
        remove(RTH);
        remove(Restart);
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
            cases[i] = new JButton("");

            // Calcul des coordonnées pour chaque bouton
            int x = startX + (i % 3) * (buttonSize + gap);
            int y = startY + (i / 3) * (buttonSize + gap);

            cases[i].setBounds(x, y, buttonSize, buttonSize);
            add(cases[i]);

            // On affecte l'évènement du bouton avec un lambda
            int finalI = i;
            setButtonsEventsHandlers(this.cases[i], () -> {
                if(client != null){
                    turnClient(finalI);
                }
                else {
                    try {
                        turnServer(finalI);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    public void removeGrid() {
        for (int i = 0; i < 9; i++) {
            cases[i].setText("");
            remove(cases[i]);
        }
        label.setText("En attente d'un joueur...");
        repaint();
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
        turn = 1;
        won = false;
        remove(Restart);
        remove(RTH);
        remove(label);
        drawGrid();
        repaint();
        label.setFont(labelFont);
        label.setBounds((getWidth()-200)/2, 20, 300 , 100);

        if (client == null) {
            label.setText("C'est votre tour");
        }
        else {
            label.setText("C'est à l'adversaire");
        }

        add(label);
        repaint();

        Server.GameStarted = true;
    }

    public void restartGame() {
        turn = 1;
        won = false;
        removeGrid();
        startGame();
    }

    public void stopGame() {
        Server.stopServer();
        removeGrid();
        mainMenu();
        repaint();
    }

    /** Méthode pour jouer un coup client */
    public void turnClient(int position) {
        if (!won) {
            // Tour du client
            if (turn == 2 && client == null || turn == 2 && Objects.equals(cases[position].getText(), "")) {
                cases[position].setText("O");
                turn = 1;

                if (client != null) {
                    if (checkWin()) {
                        System.out.println("Gagné!");
                        label.setText("Gagné!");
                        add(Restart);
                        add(RTH);
                        won = true;
                    }
                    else if (!egality()) {
                        System.out.println("C'est à l'adversaire");
                        label.setText("C'est à l'adversaire");
                    }
                    else {
                        System.out.println("Égalité!");
                        label.setText("Égalité!");
                        repaint();
                        won = true;
                        add(Restart);
                        add(RTH);
                    }
                    client.sendData(position);
                }
                else {
                    if (checkWin()) {
                        System.out.println("Perdu!");
                        label.setText("Perdu!");
                        add(Restart);
                        add(RTH);
                        won = true;
                    }
                    else if(!egality()) {
                        System.out.println("C'est votre tour");
                        label.setText("C'est votre tour");
                    }
                    else{
                        System.out.println("Égalité!");
                        label.setText("Égalité!");
                        repaint();
                        won = true;
                        add(Restart);
                        add(RTH);
                    }
                }
            }
            repaint();
        }
    }

    /** Méthode pour jouer un coup server */
    public void turnServer(int position) throws RemoteException {
        if (!won){
            // Tour du server
            if (turn == 1 && client != null || turn == 1 && Objects.equals(cases[position].getText(), "")) {
                cases[position].setText("X");
                turn = 2;

                if (client == null) {
                    if (checkWin()) {
                        System.out.println("Gagné!");
                        label.setText("Gagné!");
                        add(Restart);
                        add(RTH);
                        won = true;
                    }
                    else if(!egality()){
                        System.out.println("C'est à l'adversaire");
                        label.setText("C'est à l'adversaire");
                    }
                    else {
                        System.out.println("Égalité!");
                        label.setText("Égalité!");
                        repaint();
                        won = true;
                        add(Restart);
                        add(RTH);
                    }
                    Server.posServer.sendDataToClient(position);
                }
                else {
                    if (checkWin()) {
                        System.out.println("Perdu!");
                        label.setText("Perdu!");
                        add(Restart);
                        add(RTH);
                        won = true;
                    }
                    else if (!egality()) {
                        System.out.println("C'est à l'adversaire");
                        label.setText("C'est à l'adversaire");
                    }
                    else {
                        System.out.println("Égalité!");
                        label.setText("Égalité!");
                        repaint();
                        won = true;
                        add(Restart);
                        add(RTH);
                        return ;
                    }
                }
            }
            repaint();
        }
    }

    private boolean checkSymbol(String symbol1, String symbol2, String symbol3) {
        return !symbol1.equals("") && symbol1.equals(symbol2) && symbol1.equals(symbol3);
    }

    public boolean checkWin() {
        // Vérifier les lignes
        for (int i = 0; i < 3; i++) {
            if (checkSymbol(cases[i * 3].getText(), cases[i * 3 + 1].getText(), cases[i * 3 + 2].getText())) {
                return true;
            }
        }

        // Vérifier les colonnes
        for (int i = 0; i < 3; i++) {
            if (checkSymbol(cases[i].getText(), cases[i + 3].getText(), cases[i + 6].getText())) {
                return true;
            }
        }

        // Vérifier les diagonales
        if (checkSymbol(cases[0].getText(), cases[4].getText(), cases[8].getText()) ||
                checkSymbol(cases[2].getText(), cases[4].getText(), cases[6].getText())) {
            return true;
        }

        return false;
    }

    private static boolean egality() {
        boolean egality = false;
        for (int i = 0; i < cases.length ; i++){
            if (cases[i].getText() != "") {
                egality = true;
            }
            else {
                return false;
            }
        }

        return egality;
    }

    public static void main(String[] args){
        Tiktaktoe ttt = new Tiktaktoe();
        Grid grid = new Grid();
    }
}
