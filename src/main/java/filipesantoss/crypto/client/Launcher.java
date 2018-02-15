package filipesantoss.crypto.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {

        try {
            Client client = new Client();
            client.connect();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}