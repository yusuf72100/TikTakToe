import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Échange des données
 */
public class Position extends UnicastRemoteObject implements PositionInterface {
    int data;
    Tiktaktoe jeu;
    public Position(Tiktaktoe ttt) throws RemoteException {
        super();
        this.data = 0;
        jeu = ttt;
    }

    /**
     * Réception de la donnée
     * @param data
     * @return
     * @throws RemoteException
     */
    @Override
    public int position(int data) throws RemoteException {
        /** Gestion de la donné */
        switch (data){
            case 99:
                if (!Server.GameStarted){
                    System.out.println("Player connected");
                    jeu.startGame();
                }
                else{
                    System.out.println("Un joueur essaye de se connecter : +1 connection bloquée");
                }
                break;
            case 100:
                System.out.println("Player Disconnected");
                System.out.println("Server shutting down...");
                jeu.stopGame();
                break;
            default :
                System.out.println("Data received : " + data);
                break;
        }

        return (data);
    }
}
