package filipesantoss.crypto.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {
        Client client = null;

        try {
            client = new Client();
            client.init();
            client.start();

        } catch (IOException e) {
            System.err.println("Failed to connect to server.");

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to exchange keys.");
        }

        finally {
            if (client != null) {
                client.stop();
            }
        }
    }
}