import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Échange des données
 */
public class Position extends UnicastRemoteObject implements PositionInterface {
    int data;
    public Position() throws RemoteException {
        super();
        this.data = 0;
    }

    /**
     * Réception de la donnée
     * @param data
     * @return
     * @throws RemoteException
     */
    @Override
    public int position(int data) throws RemoteException {
        System.out.println("Data received : " + data);
        return (data);
    }
}
