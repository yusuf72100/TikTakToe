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
     * Cette méthode enverra '99' au serveur ce qui signifira une demande de lancement de partie
     */
    public Client (String Hostname, Tiktaktoe tiktaktoe) throws RemoteException, MalformedURLException, NotBoundException {
        hostname = Hostname;
        ttt = tiktaktoe;
        client = new Position(tiktaktoe);
        server = new Position(tiktaktoe);
        server = (PositionInterface) Naming.lookup("rmi://" + this.hostname + ":1099/Position");
    }

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

    /** Méthode d'arrêt de la communication du client */
    public void stopClient() throws RemoteException {
        try {
            client.stopClient();
        } catch (RemoteException e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
