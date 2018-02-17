package filipesantoss.crypto.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {
        Server server = null;

        try {
            server = new Server();
            server.start();

        } catch (IOException e) {
            System.err.println("Failed to accept client connection.");

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to exchange keys.");
        }

        finally {
            if (server != null) {
                server.stop();
            }
        }
    }
}
