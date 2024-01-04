import java.io.FileWriter;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * A simple TCP server for the auction that can simultaneously work with 30 connected clients.
 */
public class Server {

    /**
     * Stores all lots added by clients.
     */
    private static final HashMap<String, Item> items = new HashMap<>();

    /**
     * Tries to make a bid on the specified item.
     *
     * @param itemName the name of the item on which the bid is made.
     * @param bid      the value of the bid.
     * @param bidderIP the IP address of the client who makes the bid.
     * @return 0 - Failure, 1 - Rejected, 2 - Accepted.
     */
    public static synchronized int makeBid(String itemName, double bid, String bidderIP) {
        if (items.containsKey(itemName) && bid > 0) {
            return items.get(itemName).newBid(bid, bidderIP) ? 2 : 1;
        }
        return 0;
    }

    /**
     * Tries to add a new item to the auction list.
     *
     * @param itemName the name of the added item.
     * @return true if the item was successfully added, otherwise false.
     */
    public static synchronized boolean addItem(String itemName) {
        if (items.containsKey(itemName)) {
            return false;
        }
        items.put(itemName, new Item(itemName));
        return true;
    }

    /**
     * Returns a string containing information about all items currently up for auction.
     *
     * @return the string containing information about all items currently up for auction.
     */
    public static synchronized String getItemsList() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (Map.Entry<String, Item> item : items.entrySet()) {
            stringBuilder.append(item.getValue());
            if (i++ < items.size() - 1) {
                stringBuilder.append('=');
            }
        }
        return stringBuilder.toString();
    }

    /**
     * Writes data to the log.txt file.
     *
     * @param data the data to write to the log.txt file.
     */
    public static synchronized void writeToLog(String data) {
        try (FileWriter writer = new FileWriter("log.txt", true)) {
            writer.append(data).append("\n");
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the server and registers connections from clients.
     *
     * @param args no arguments are used.
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            System.out.println("Server is running.");
            Executor executor = Executors.newFixedThreadPool(30);
            while (true) {
                executor.execute(new ServerClient(serverSocket.accept()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
