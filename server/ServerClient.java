import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Manages the connection between the client and the server.
 */
public class ServerClient implements Runnable {
    /**
     * The client socket.
     */
    private Socket socket = null;

    /**
     * Constructs a new ServerClient with the provided client socket.
     *
     * @param socket the client socket.
     */
    public ServerClient(Socket socket) {
        this.socket = socket;
    }

    /**
     * Runs the main client interaction logic.
     */
    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            String request = reader.readLine();
            boolean validRequest = false;
            if (request.startsWith("SHOW")) {
                validRequest = true;
                writer.println("LIST=" + Server.getItemsList());
            } else if (request.startsWith("ITEM")) {
                String[] data = request.split("=");
                validRequest = true;
                writer.println(Server.addItem(data[1]) ? "Success" : "Failure");
            } else if (request.startsWith("BID")) {
                String[] data = request.split("=");
                String[] responses = {"Failure", "Rejected", "Accepted"};
                validRequest = true;
                int response = Server.makeBid(data[1], Double.parseDouble(data[2]),
                        socket.getInetAddress().getHostAddress());
                writer.println(responses[response]);
            }
            if (validRequest) {
                saveLogData(request);
            }
        } catch (Exception e) {
            if (e.getMessage().equals("Socket closed") || e.getMessage().equals("Connection reset")) {
                System.out.println(e.getMessage() + " " + socket.getInetAddress().getHostAddress());
            } else {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the request from the client to log file.
     *
     * @param request the request from the client.
     */
    private void saveLogData(String request) {
        Server.writeToLog(LocalDate.now() + "|" + LocalTime.now() + "|"
                + socket.getInetAddress().getHostAddress() + "|" + request);
    }
}
