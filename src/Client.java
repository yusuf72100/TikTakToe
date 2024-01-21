import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static PositionInterface posClient;

    /**
     * Cette méthode enverra '99' au serveur ce qui signifira une demande de lancement de partie
     */
    public static void startClient(){
        sendData(99);
    }

    public static void sendData(int position){
        try{
            posClient = (PositionInterface) Naming.lookup("rmi://localhost:1099/Position");
            int result = posClient.position (position);
        }
        catch (Exception e)
        {
            System.out.println ("Erreur d'accès à l'objet distant.");
            System.out.println (e.toString());
        }
    }

    /** Méthode d'arrêt de la communication du client */
    public static void stopClient() {
        try {
            if (posClient != null) {
                UnicastRemoteObject.unexportObject(posClient, true);
                System.out.println("Client arrêté");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
