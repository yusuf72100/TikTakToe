import java.net.InetAddress;
import java.net.NetworkInterface;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Enumeration;

/**
 * Cette classe gèrera la communication point à point par socket
 */
public class Server {
    public static boolean GameStarted;
    private static Registry registry;
    public static  Position posServer;
    private static Tiktaktoe tiktaktoe;

    public static void startServer(Tiktaktoe ttt) {
        try {
            GameStarted = false;
            /** Récupération automatique de l'adresse ip locale */
            /*try {
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface iface = interfaces.nextElement();
                    Enumeration<InetAddress> addresses = iface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress addr = addresses.nextElement();
                        System.out.println("  IP: " + addr.getHostAddress());
                        System.setProperty("java.rmi.server.hostname",addr.getHostAddress());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/

            posServer = new Position(ttt);
            tiktaktoe = ttt;
            System.out.println( "Serveur : Construction de l'implementation");
            System.out.println("Objet Position lié dans le RMIregistry");
            if (registry == null) registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/Position", posServer);
            System.out.println("Attente des invocations des clients ...");

        } catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet Reverse");
            System.out.println(e.toString());
        }
    }

    public static void stopServer() {
        try {
            GameStarted = false;

            if (posServer != null) {
                posServer.sendDataToClient(101);
                UnicastRemoteObject.unexportObject(posServer, true);
            }

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
