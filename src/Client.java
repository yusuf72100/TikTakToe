import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static Tiktaktoe ttt;
    private String hostname;
    public PositionInterface server;
    public PositionInterface client;

    /**
     * Initialisation des instances client
     * @param Hostname
     * @param tiktaktoe
     * @throws RemoteException
     * @throws MalformedURLException
     * @throws NotBoundException
     */
    public Client (String Hostname, Tiktaktoe tiktaktoe) throws RemoteException, MalformedURLException, NotBoundException {
        hostname = Hostname;
        ttt = tiktaktoe;
        client = new Position(tiktaktoe);
        server = new Position(tiktaktoe);
        server = (PositionInterface) Naming.lookup("rmi://" + this.hostname + ":1099/Position");
    }

    /**
     * Envoyer une donnée au serveur
     * @param position
     */
    public void sendData(int position){
        try {
            int result = server.position(position);

        } catch (ConnectException ce) {
            System.out.println("Erreur de connexion au serveur. Assurez-vous que le serveur est en cours d'exécution.");
            System.out.println(ce.toString());

        } catch (Exception e) {
            System.out.println("Erreur générale lors de l'accès à l'objet distant.");
            System.out.println(e.toString());
        }
    }

    /**
     * Arrêt de la communication client
     * @throws RemoteException
     */
    public void stopClient() throws RemoteException {
        try {
            /**
             * on envoi 100 pour signaler au serveur qu'on se déconnecte
             */
            sendData(100);
            client.stopClient();
            client = null;
        } catch (RemoteException e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
