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

    /**
     * Initialisation des instances de l'objet partagé
     * @param ttt
     * @throws RemoteException
     */
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
                /**
                 * 99 fait référence à une demande de connection
                 */
                /**
                 * Si aucune partie n'est en cours et qu'aucun client n'est connecté alors on accepte la connection et on lance une partie
                 */
                if (!Server.GameStarted){
                    if ((connectedClients+1) == 1){
                        System.out.println("Game started!");
                        jeu.startGame();
                    }
                    connectedClients++;
                    System.out.println(connectedClients + " player(s) connected");
                }
                /**
                 * Sinon on refuse la connection
                 */
                else{
                    System.out.println("Un joueur essaye de se connecter : +1 connection bloquée");
                }
                break;
            case 100:
                /**
                 * 100 fait référence à une déconnection du client
                 */
                System.out.println("Player Disconnected");
                System.out.println("Server shutting down...");
                jeu.stopGame();
                break;
            /** On recommence la partie */
            case 500:
                /**
                 * 500 fait référence à un vote client
                 */
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

    /**
     * Réception de données envoyées par le serveur
     * @param position
     * @return
     * @throws RemoteException
     */
    public int receiveData(int position) throws RemoteException {
        switch (position) {
            /**
             * 101 signifie qu'on stop le jeu
             */
            case 101:
                System.out.println("Server closed!");
                jeu.stopGame();
                break;
            /** On recommence la partie */
            case 500:
                /**
                 * 500 fait référence à un vote server
                 */
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

    /**
     * Stocker l'instance du client sur le serveur
     * @param Client
     * @throws RemoteException
     */
    public void registerClient(PositionInterface Client) throws RemoteException {
        client = Client;
        System.out.println("Client registered.");
    }

    /**
     * Envoi de données du serveur au client
     * @param position
     * @throws RemoteException
     */
    public void sendDataToClient(int position) throws RemoteException {
        client.receiveData(position);
    }

    /**
     * Incrémenter le compteur de votes pour redémarrer la partie une fois celle-ci terminée
     * @throws RemoteException
     */
    public void addVote() throws RemoteException {
        nb_votant++;
    }

    /**
     * Obtenir le nombre de votants
     * @return
     * @throws RemoteException
     */
    public int getVotes() throws RemoteException {
        return nb_votant;
    }

    /**
     * Affecter le nombre de votants à la valeur souhaitée
     * @param position
     * @throws RemoteException
     */
    public void setVotes(int position) throws RemoteException {
        nb_votant = position;
    }

    /**
     * Demande arrêt serveur
     * @throws RemoteException
     */
    public void stopServer() throws RemoteException {
        try {
            UnicastRemoteObject.unexportObject(this, true);
            System.out.println("Serveur arrêté");
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du serveur");
            System.out.println(e.toString());
        }
    }

    /**
     * Demander arrêt client
     * @throws RemoteException
     */
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
