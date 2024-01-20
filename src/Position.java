import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Position extends UnicastRemoteObject implements PositionInterface {

    public Position() throws RemoteException {
        super();
    }

    @Override
    public int position(int position) throws RemoteException {
        return (position);
    }
}
