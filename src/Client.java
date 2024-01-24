import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static PositionInterface posClient;
    private static Tiktaktoe ttt;
    private static String hostname;

    /**
     * Cette méthode enverra '99' au serveur ce qui signifira une demande de lancement de partie
     */
    public Client (String Hostname, Tiktaktoe tiktaktoe) {
        sendData(99);
        hostname = Hostname;
        ttt = tiktaktoe;
    }

    public void sendData(int position){
        try{
            System.out.println(hostname);
            posClient = (PositionInterface) Naming.lookup("rmi://" + hostname + ":1099/Position");
            PositionInterface client = new Position(ttt);
            String result = posClient.setClient (client);
        }
        catch (Exception e)
        {
            System.out.println ("Erreur d'accès à l'objet distant.");
            System.out.println (e.toString());
        }
    }

    /** Méthode d'arrêt de la communication du client */
    public void stopClient() {
        try {
            if (posClient != null) {
                UnicastRemoteObject.unexportObject(posClient, true);
                System.out.println("Client arrêté");
                this.sendData(100);
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
