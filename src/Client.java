import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

public class Client {
    private static PositionInterface pos;
    public static void sendData(int position){
        try{
            pos = (PositionInterface) Naming.lookup("rmi://localhost:1099/Position");
            int result = pos.position (position);
            System.out.println ("Datas : " + result);
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
            if (pos != null) {
                UnicastRemoteObject.unexportObject(pos, true);
                System.out.println("Client arrêté");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du client");
            System.out.println(e.toString());
        }
    }
}
