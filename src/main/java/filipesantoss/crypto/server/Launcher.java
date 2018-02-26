package filipesantoss.crypto.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {
        Server server = null;

        if (args.length < 1) {
            System.out.println("Usage: java -jar basic-encryption-chat-server.jar <port>");
            System.exit(1);
        }

        try {
            server = new Server(Integer.parseInt(args[0]));
            server.start();

        } catch (IOException e) {
            System.err.println(e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to exchange keys.");
        }

        catch (NumberFormatException e) {
            System.err.println(args[0] + " is not a valid port number.");
        }

        finally {
            if (server != null) {
                server.stop();
            }
        }
    }
}
