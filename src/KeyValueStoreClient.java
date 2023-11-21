import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Key Value Store Client
 */
public class KeyValueStoreClient {

    private KeyValueStoreClient() {}

    public static void main(String[] args) {
        System.out.println("Starting the Key Value Store Client, your port must be either 5001, 5002, 5003, 5004, or 5005.\n");
        try {
            if (args.length > 2) {
                System.out.println("Usage: java KeyValueStoreClient <localhost> <port number: 5001, 5002, 5003, 5004, or 5005>");
                System.exit(1);
            }
            // parse the argument input
            String localhost = args[0];
            int clientPort = Integer.parseInt(args[1]);
            // Getting the registry
            Registry registry = LocateRegistry.getRegistry(localhost, clientPort);
            // looks up key value store from the registry
            // and casts it to the KeyValueStore interface allowing client to invoke the remote methods
            // Establish connections to the remote KeyValueStore object on the server
            KeyValueStore participant = (KeyValueStore) registry.lookup("KeyValueStore");

            // Starting the transaction
            participant.requestTxn("Client requested transaction...");

            System.out.println("Enter at least 5 PUTs, 5 GETs, 5 DELETEs operation.\nHere is an example:");

            String response = "", timestamp = "";

            // to pre-populate the Key-Value store with data and a set of keys
            String[] prepopulate = {"a", "b", "c", "d", "e", "f"};
            for (int i = 0; i < 5; i ++) {
                String txn = "PUT " + prepopulate[i] + " " + prepopulate[i + 1];
                System.out.println(timestamp + ", " + txn);
                timestamp = Utils.getCurrentTimestamp();
                if (participant.requestTxn(txn)) {
                    response = participant.response();
                }
                System.out.println(timestamp + ", " + response.substring(response.indexOf("*") + 1));
            }

            System.out.println(timestamp + ", Enter operation:\nPUT <key> <value> or GET <key> or DELETE <key>");

            // perform at least 5 GETs, 5 PUTs, 5 DELETES
            while (true) {
                // Get the current timestamp
                timestamp = Utils.getCurrentTimestamp();
                String userInput = System.console().readLine();
                System.out.println(timestamp + ", Sending: " + userInput);

                // Split the user input
                String[] inputTokens = userInput.split("\\s+");
                if (participant.requestTxn(userInput)){
                    if (inputTokens.length > 0) {
                        String operation = inputTokens[0].toUpperCase();

                        switch (operation) {
                            case "GET" -> {
                                if (inputTokens.length == 2) {
                                    response = participant.response();
                                } else {
                                    response = "Make sure there's one key to perform GET operation.";
                                }
                            }
                            case "PUT" -> {
                                if (inputTokens.length == 3) {
                                    response = participant.response();
                                } else {
                                    response = "Make sure there's one key value to perform PUT operation.";
                                }
                            }
                            case "DELETE" -> {
                                if (inputTokens.length == 2) {
                                    response = participant.response();
                                } else {
                                    response = "Make sure there's one key to perform DELETE operation.";
                                }
                            }
                            default -> response = "Received an unknown operation. Try again. Or you didn't want to shutdown.";
                        }
                }
                    int indexCleanResponse = response.indexOf("*");
                    System.out.println(timestamp + ", " + response.substring(indexCleanResponse + 1));
                }
                System.out.println(timestamp + ", Enter operation:\nPUT <key> <value> or GET <key> or DELETE <key>");
            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
