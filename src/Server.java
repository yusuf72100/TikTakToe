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
    static Tiktaktoe tiktaktoe;
    static Position posServer;

    public Server (Tiktaktoe ttt) throws RemoteException {
        tiktaktoe = ttt;
        posServer = new Position(ttt);
    }

    public static void startServer(Tiktaktoe tiktaktoe) {
        try {
            GameStarted = false;
            /** Récupération automatique de l'adresse ip locale */
            try {
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
            }

            System.out.println( "Serveur : Construction de l'implementation");
            System.out.println("Objet Position lié dans le RMIregistry");
            registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/Position", posServer);
            System.out.println("Attente des invocations des clients ...");

        } catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet Reverse");
            System.out.println(e.toString());
        }
    }

    public static String sendData(int position) throws RemoteException {
        PositionInterface posServer = new Position(tiktaktoe);
        String resultat = posServer.sendToClient(position);

        return resultat;
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
