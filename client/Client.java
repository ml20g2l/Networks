import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A simple TCP client that can connect to the auction server and send one of three requests to it.
 */
public class Client {

    /**
     * Tries to connect to the server and sends the specified command if the connection succeeds.
     *
     * @param command the command to send to the server.
     */
    public static void sendCommand(String command) {
        try (Socket socket = new Socket("localhost", 6666)) {
            new PrintWriter(socket.getOutputStream(), true).println(command);
            String answer = new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
            if (answer.startsWith("LIST")) {
                String[] elements = answer.split("=");
                if (elements.length == 1) {
                    System.out.println("There are currently no items in this auction.");
                } else {
                    for (int i = 1; i < elements.length; i++) {
                        System.out.println(elements[i]);
                    }
                }
            } else {
                System.out.println(answer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Runs the program and processes the arguments passed at startup,
     * if the arguments are valid, it tries to connect to the server,
     * otherwise it displays an error message.
     *
     * @param args the arguments passed when starting the program.
     */
    public static void main(String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("show")) {
            sendCommand("SHOW");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("item")) {
            sendCommand("ITEM=" + args[1]);
        } else if (args.length == 3 && args[0].equalsIgnoreCase("bid")) {
            try {
                Double.parseDouble(args[2]);
                sendCommand("BID=" + args[1] + "=" + args[2]);
            } catch (NumberFormatException e) {
                System.out.println("Error: bid <name> <value>(must be a positive real number).");
            }
        } else {
            System.out.println("Available arguments:\n1. show\n2. item <name>\n3. bid <name> <value>");
        }
    }
}
