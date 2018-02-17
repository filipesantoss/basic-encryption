package filipesantoss.crypto.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {

        try {
            Server server = new Server();
            server.start();

        } catch (IOException e) {
            System.err.println("Failed to accept client connection.");
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to create KeyChain.");
            e.printStackTrace();
        }
    }
}
