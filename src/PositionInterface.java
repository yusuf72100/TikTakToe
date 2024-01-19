import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PositionInterface extends Remote {
    int position (int position) throws RemoteException;
}
