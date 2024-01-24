import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface de l'échange des données
 */
public interface PositionInterface extends Remote {
    public int position (int position) throws RemoteException;
    public int receiveData(int position);
    public void registerClient(PositionInterface Client) throws RemoteException;
    public void sendDataToClient(int position) throws RemoteException;
}
