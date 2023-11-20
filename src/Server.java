import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        try {
            System.out.println("Starting the server");
            if (args.length != 2) {
                System.err.println("Usage: java KeyValueStoreServer <IP address> <serverPort>");
                System.exit(1);
            }
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            List<String> participantHosts = Arrays.asList("localhost", "localhost", "localhost", "localhost", "localhost");
            List<Integer> participantPorts = Arrays.asList(5001, 5002, 5003, 5004, 5005);
            // Start the participants
            // initialize 5 instance of the participants and bind them to RMI registry
            for (int i = 0; i < 5; i++) {
                KeyValueStoreImpl participant = new KeyValueStoreImpl();
                Registry participantRegistry = LocateRegistry.createRegistry(participantPorts.get(i));
                participantRegistry.bind("KeyValueStore", participant);
            }

            // Start the coordinator
            // initialize 1 instance of the coordinator and bind it to the RMI registry
            CoordinatorImpl coordinator = new CoordinatorImpl();
            Registry coordinatorRegistry = LocateRegistry.createRegistry(port);
            coordinatorRegistry.bind("Coordinator", coordinator);

            // Update each instance of the participants with the instance of the coordinator
            // only after you have created the coordinator.
            for (int i = 0; i < participantHosts.size(); i++) {
                System.out.println(Utils.getCurrentTimestamp() + ", participant: " +  participantHosts.get(i) + " " + participantPorts.get(i));
                Registry registry = LocateRegistry.getRegistry(participantHosts.get(i), participantPorts.get(i));
                KeyValueStore participant = (KeyValueStore) registry.lookup("KeyValueStore");
                coordinator.addParticipant(participant);

                Registry coordinatorReg = LocateRegistry.getRegistry(host, port);
                Coordinator c = (Coordinator) coordinatorReg.lookup("Coordinator");
                participant.setCoordinator(c);
            }

            System.out.println("Servers ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }
}