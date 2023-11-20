import java.rmi.Remote;
import java.rmi.RemoteException;

public interface KeyValueStore extends Remote {
    String get(String key) throws RemoteException;
    String put(String key, String value) throws RemoteException;
    String delete(String key) throws RemoteException;

    boolean prepare(String transaction) throws RemoteException;
    String commit() throws RemoteException;
    String abort() throws RemoteException;
    void setCoordinator(Coordinator coord) throws RemoteException;
    boolean requestTxn(String transaction) throws RemoteException;
    String response() throws RemoteException;
}