import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static Tiktaktoe ttt;
    private String hostname;

    /**
     * Cette méthode enverra '99' au serveur ce qui signifira une demande de lancement de partie
     */
    public Client (String Hostname, Tiktaktoe tiktaktoe) {
        hostname = Hostname;
        ttt = tiktaktoe;
    }

    public void sendData(int position){
        try {
            System.out.println(hostname);
            PositionInterface posClient = (PositionInterface) Naming.lookup("rmi://" + this.hostname + ":1099/Position");
            int result = posClient.position(position);
            PositionInterface client = new Position(ttt);
            String resultat = posClient.setClient(client);

        } catch (java.rmi.ConnectException ce) {
            System.out.println("Erreur de connexion au serveur. Assurez-vous que le serveur est en cours d'exécution.");
            System.out.println(ce.toString());

        } catch (java.rmi.NotBoundException nbe) {
            System.out.println("Erreur: Position n'est pas lié dans le RMIregistry.");
            System.out.println(nbe.toString());

        } catch (Exception e) {
            System.out.println("Erreur générale lors de l'accès à l'objet distant.");
            System.out.println(e.toString());
        }
    }

    /** Méthode d'arrêt de la communication du client */
    public void stopClient() {
        System.exit(0);
    }
}
