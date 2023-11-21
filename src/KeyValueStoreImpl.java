import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * My Participant Implementation
 */
public class KeyValueStoreImpl extends UnicastRemoteObject implements KeyValueStore {
    private ConcurrentHashMap<String, String> operation = new ConcurrentHashMap<>();
    private String transaction;
    private Coordinator coordinator;
    private String response;

    public KeyValueStoreImpl() throws RemoteException {
        super();
    }

    @Override
    public boolean prepare(String transaction) throws RemoteException {
        this.transaction = transaction;
        // condition to abort if deleting something that doesn't exist
        if (this.transaction.split(" ")[0].equalsIgnoreCase("DELETE")) {
            if (!operation.containsKey(this.transaction.split(" ")[1])){
                return false;
            }
        }
        return true;
    }

    @Override
    public String commit() throws RemoteException {
        String[] transactionParts = this.transaction.split(" ");

        String op = "";
        String key = "";
        String value = "";

        if (transactionParts.length > 0) {
            op = transactionParts[0];
        }
        if (transactionParts.length > 1) {
            key = transactionParts[1];
        }
        if (transactionParts.length > 2) {
            value = transactionParts[2];
        }
        if (op.equalsIgnoreCase("PUT")) {
            this.response = put(key, value);
        } else if (op.equalsIgnoreCase("DELETE")) {
            this.response = delete(key);
        } else if (op.equalsIgnoreCase("GET")) {
            this.response = get(key);
        }
        return "Committing: " + this.transaction;
    }

    @Override
    public String get(String key) throws RemoteException {
        if (!operation.containsKey(key)) {
            return "Key does not exist.";
        } else {
            operation.get(key);
            return "GETs*Here is your value " + operation.get(key);
        }
    }

    @Override
    public String put(String key, String value) throws RemoteException {
        if (operation.containsKey(key)) {
            return "Key already exist, try another key.";
        } else {
            operation.putIfAbsent(key, value);
            return "PUTs*OK saved operation: {key= " + key + ", value= " + value + "}\n" + "Current operations: " + operation;
        }
    }

    @Override
    public String delete(String key) throws RemoteException {
        if (!operation.containsKey(key)) {
            return "Key does not exist.\noperations left: " + operation;
        } else {
            operation.remove(key);
            return "DELETEs*Deleted key as requested.\nOperations left: "+ operation;
        }
    }

    @Override
    public String abort() throws RemoteException {
        return "Aborting this transaction";
    }

    @Override
    public void setCoordinator(Coordinator c) throws RemoteException{
        this.coordinator = c;
    }

    @Override
    public boolean requestTxn(String transaction) throws RemoteException {
        this.transaction = transaction;
        return coordinator.prepareTransaction(this.transaction);
    }

    @Override
    public String response() throws RemoteException{
        return this.response;
    }
}