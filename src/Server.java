import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Cette classe gèrera la communication point à point par socket
 */
public class Server {
    public static boolean GameStarted;
    private static Registry registry;
    public static Position posServer;

    public static void startServer(Tiktaktoe ttt) {
        try {
            GameStarted = false;
            System.out.println( "Serveur : Construction de l'implementation");
            posServer = new Position(ttt);
            System.out.println("Objet Position lié dans le RMIregistry");
            registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/Position", posServer);
            System.out.println("Attente des invocations des clients ...");

        } catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet Reverse");
            System.out.println(e.toString());
        }
    }

    public static void stopServer() {
        try {
            if (registry != null) {
                registry.unbind("rmi://localhost:1099/Position");
                UnicastRemoteObject.unexportObject(registry, true);
                System.out.println("Serveur arrêté");
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'arrêt du serveur");
            System.out.println(e.toString());
        }
    }
}
