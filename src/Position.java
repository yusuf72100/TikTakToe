import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Échange des données
 */
public class Position extends UnicastRemoteObject implements PositionInterface {
    int data;
    int connectedClients;
    public PositionInterface client;
    Tiktaktoe jeu;
    public Position(Tiktaktoe ttt) throws RemoteException {
        super();
        this.data = 0;
        jeu = ttt;
        connectedClients = 0;
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
                    if ((connectedClients+1) == 1){
                        System.out.println("Game started");
                        jeu.startGame();
                    }
                    connectedClients++;
                    System.out.println(connectedClients + " player(s) connected");
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

    public int receiveData(int position) {
        switch (position) {
            case 101:
                System.out.println("Server closed!");
                jeu.stopGame();
                break;
        }
        System.out.println("Data received from server : " + position);
        return position;
    }

    public void registerClient(PositionInterface Client) throws RemoteException {
        client = Client;
        System.out.println("Client registered.");
    }

    public void sendDataToClient(int position) throws RemoteException {
        client.receiveData(position);
    }
}
