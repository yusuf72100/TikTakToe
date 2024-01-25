import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Échange des données
 */
public class Position extends UnicastRemoteObject implements PositionInterface {
    int data;
    int connectedClients;
    private int nb_votant;
    public PositionInterface client;
    Tiktaktoe jeu;
    public Position(Tiktaktoe ttt) throws RemoteException {
        super();
        nb_votant = 0;
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
    public int position(int data) throws RemoteException {
        /** Gestion de la donnée */
        switch (data){
            case 99:
                if (!Server.GameStarted){
                    if ((connectedClients+1) == 1){
                        System.out.println("Game started!");
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
            /** On recommence la partie */
            case 500:
                addVote();
                System.out.println("Votes number : " + nb_votant);
                if (nb_votant >= 2) {
                    System.out.println("Restarting the session");
                    jeu.restartGame();
                    setVotes(0);
                }
                break;
            default :
                System.out.println("Data received : " + data);
                jeu.turnClient(data);
                break;
        }
        return (data);
    }

    /** Réception de données du client */
    public int receiveData(int position) throws RemoteException {
        switch (position) {
            case 101:
                System.out.println("Server closed!");
                jeu.stopGame();
                break;
            /** On recommence la partie */
            case 500:
                addVote();
                System.out.println("Votes number : " + nb_votant);
                if (nb_votant >= 2) {
                    System.out.println("Restarting the session");
                    jeu.restartGame();
                    setVotes(0);
                }
                break;
            default:
                jeu.turnServer(position);
                break;
        }
        System.out.println("Data received from server : " + position);
        return position;
    }

    /** Cette méthode permettra de stocker l'instance du client sur le serveur */
    public void registerClient(PositionInterface Client) throws RemoteException {
        client = Client;
        System.out.println("Client registered.");
    }

    /** Méthpde d'envoi de données du serveur au client */
    public void sendDataToClient(int position) throws RemoteException {
        client.receiveData(position);
    }

    public void addVote() throws RemoteException {
        nb_votant++;
    }

    public int getVotes() throws RemoteException {
        return nb_votant;
    }

    public void setVotes(int position) throws RemoteException {
        nb_votant = position;
    }

    public void stopServer() throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("Serveur arrêté");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du serveur");
            System.out.println(e.toString());
        }
    }

    public void stopClient() throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("Client arrêté");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
