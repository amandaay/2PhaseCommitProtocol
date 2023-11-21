/*Amanda Au-Yeung*/
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Coordinator interface
 */
public interface Coordinator extends Remote {
    boolean prepareTransaction(String transaction) throws RemoteException;
    void addParticipant(KeyValueStore participant) throws RemoteException;
}