import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de l'échange des données
 */
public interface PositionInterface extends Remote {
    public int position (int position) throws RemoteException;
    public String setClient(PositionInterface client) throws RemoteException;
    public String sendToClient(int position) throws RemoteException;
}
