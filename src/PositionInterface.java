import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de l'échange des données
 */
public interface PositionInterface extends Remote {
    int position (int position) throws RemoteException;
    int receiveData(int position) throws RemoteException;
    void registerClient(PositionInterface Client) throws RemoteException;
    void sendDataToClient(int position) throws RemoteException;
}
