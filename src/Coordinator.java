import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Coordinator extends Remote {
    boolean prepareTransaction(String transaction) throws RemoteException;
    void addParticipant(KeyValueStore participant) throws RemoteException;
}