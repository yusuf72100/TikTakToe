import java.io.*;
import java.net.*;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Cette classe gèrera la communication point à point par socket
 */
public class Server {
    public static void startServer(){
        try {
            System.out.println( "Serveur : Construction de l'implementation");
            Position pos = new Position();
            System.out.println("Objet Position lié dans le RMIregistry");
            Registry registry = LocateRegistry.createRegistry(1099);
            Naming.rebind("rmi://localhost:1099/Position", pos);
            System.out.println("Attente des invocations des clients ...");

        } catch (Exception e) {
            System.out.println("Erreur de liaison de l'objet Reverse");
            System.out.println(e.toString());
        }
    }

}
