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
        
        setButtonsEventsHandlers();
        setLocationRelativeTo(null);    // place la fenêtre au milieu de l'écran
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }   

    /**
     * Cette méthode initialisera l'affichage de la grille
     */
    private void drawGrid() {
        for (int i = 0; i < 9; i++) {
            cases[i] = new JButton((i + 1) + "");
            cases[i].setBounds(80 * (i % 3), 80 * (i / 3), 80, 80);
            add(cases[i]);
        }

        /** On affecte pour chaque bouton un évènement */
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            this.cases[i].addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    Client.sendData(finalI);
                }
            });
        }
    }

    /**
     * Cette méthode initialisera les évènements de chaque bouton
     */
    private void setButtonsEventsHandlers() {
        Jouer.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                remove(Jouer);
                remove(Quitter);
                repaint();
                drawGrid();
                Server.startServer();
            }
        });

        Quitter.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });
    }

    public static void main(String[] args){
        Tiktaktoe ttt = new Tiktaktoe();
        Grid grid = new Grid();
        Server socket = new Server();

        /*
        socket.startServer();
        socket.sendToServer("yo");
        */
    }
}
