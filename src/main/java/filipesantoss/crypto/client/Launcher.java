package filipesantoss.crypto.client;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Launcher {

    public static void main(String[] args) {
        Client client = null;

        if (args.length < 2) {
            System.out.println("Usage: java -jar basic-encryption-client.jar <host> <port>");
            System.exit(1);
        }

        try {
            client = new Client(args[0], Integer.parseInt(args[1]));
            client.init();
            client.start();

        } catch (IOException e) {
            System.err.println("Failed to connect to " + args[0] + ":" + args[1]);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to exchange keys.");
        }

        catch (NumberFormatException e) {
            System.err.println(args[0] + " is not a valid port number.");
        }

        finally {
            if (client != null) {
                client.stop();
            }
        }
    }
}