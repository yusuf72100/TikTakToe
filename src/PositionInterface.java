import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de l'échange des données
 */
public interface PositionInterface extends Remote {
    int position (int position) throws RemoteException;
}
