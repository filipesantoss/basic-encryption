package filipesantoss.crypto.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {

        try {
            Client client = new Client();
            client.init();

        } catch (IOException e) {
            System.err.println("Failed to connect to server.");
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to exchange keys.");
            e.printStackTrace();
        }
    }
}