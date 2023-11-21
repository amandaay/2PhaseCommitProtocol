/*Amanda Au-Yeung*/
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * KeyValueStore is my Participant/Server Remote Object
 */
public interface KeyValueStore extends Remote {
    /**
     * get operation for key value pair
     * @param key client entry
     * @return response
     * @throws RemoteException
     */
    String get(String key) throws RemoteException;

    /**
     * Put operation for key value
     * @param key client entry
     * @param value client entry
     * @return response
     * @throws RemoteException
     */
    String put(String key, String value) throws RemoteException;

    /**
     * Delete operation
     * @param key client entry
     * @return response
     * @throws RemoteException
     */
    String delete(String key) throws RemoteException;

    /**
     * Prepare transaction to commit or not
     * @param transaction current transaction
     * @return true if vote to commit, false if to abort
     * @throws RemoteException
     */
    boolean prepare(String transaction) throws RemoteException;

    /**
     * Commit operations to all replicas
     * @return commit message
     * @throws RemoteException
     */
    String commit() throws RemoteException;

    /**
     * Abort operation
     * @return abort message
     * @throws RemoteException
     */
    String abort() throws RemoteException;

    /**
     * set current coordinator
     * @param coord coordinator
     * @throws RemoteException
     */
    void setCoordinator(Coordinator coord) throws RemoteException;

    /**
     * use for each request of transaction
     * @param transaction user input
     * @return true if it continues to vote to commit, false if abort
     * @throws RemoteException
     */
    boolean requestTxn(String transaction) throws RemoteException;

    /**
     * to print out response from server to client
     * @return response
     * @throws RemoteException
     */
    String response() throws RemoteException;
}