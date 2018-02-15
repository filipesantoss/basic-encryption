package filipesantoss.crypto.server;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {

        try {
            Server server = new Server();
            server.init();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
