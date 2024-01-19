import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Cette classe gèrera la communication point à point par socket
 */
public class Server {
    private static Registry registry;
    public static void startServer() {
        try {
            System.out.println( "Serveur : Construction de l'implementation");
            Position pos = new Position();
            System.out.println("Objet Position lié dans le RMIregistry");
            registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/Position", pos);
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
            System.exit(0);
        }
    }
}
