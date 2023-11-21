/*Amanda Au-Yeung*/
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Coordinator implementation
 */
public class CoordinatorImpl extends UnicastRemoteObject implements Coordinator {

    private List<KeyValueStore> participants;

    public CoordinatorImpl() throws RemoteException {
        super();
        participants = new ArrayList<>();
    }

    @Override
    public boolean prepareTransaction(String transaction) throws RemoteException {
        System.out.println(Utils.getCurrentTimestamp() + ", Receiving: " + transaction);
        for (KeyValueStore participant : participants) {
            if (!participant.prepare(transaction)) {
                // if any participants aborts, it returns false
                System.out.println(Utils.getCurrentTimestamp() + ", " + participant.abort());
                return false;
            }
        }
        for (KeyValueStore participant : participants) {
            System.out.println(Utils.getCurrentTimestamp() + ", " + participant.commit());
        }
        // if all of txn commits, return true
        System.out.println("Committing the transaction");
        return true;
    }

    public void addParticipant(KeyValueStore participant) throws RemoteException{
        this.participants.add(participant);
    }
}