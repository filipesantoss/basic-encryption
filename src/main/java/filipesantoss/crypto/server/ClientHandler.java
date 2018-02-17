package filipesantoss.crypto.server;

import filipesantoss.crypto.util.Constants;
import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final Server server;
    private final KeyChain keyChain;
    private ObjectOutputStream output;

    public ClientHandler(Socket client, Key symmetric, Server server) throws NoSuchAlgorithmException {
        this.client = client;
        keyChain = new KeyChain(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        keyChain.setSymmetric(symmetric);
        this.server = server;
    }

    @Override
    public void run() {
        try {
            output = new ObjectOutputStream(client.getOutputStream());
            ObjectInputStream input = new ObjectInputStream(client.getInputStream());

            exchangePublicKeys(output, input);
            sendSymmetricKey(output);
            server.add(this);

            while (true) {

                Message message = Stream.read(input);

                if (message == null) {
                    break;
                }

                server.broadcast(this, message);
            }

            server.remove(this);
            stop();

        } catch (IOException e) {
            System.err.println("Failed to setup streams.");
        }
    }

    private void stop() {
        Stream.close(client);
    }

    private void exchangePublicKeys(ObjectOutputStream output, ObjectInputStream input) {
        Stream.write(output, keyChain.getPublic());

        Key foreign = Stream.read(input);
        keyChain.setForeign(foreign);
    }

    private void sendSymmetricKey(ObjectOutputStream output) {
        Message<Key> sealedSymmetric = keyChain.encryptWithForeign(keyChain.getSymmetric());
        Stream.write(output, sealedSymmetric);
    }

    public void write(Message message) {
        Stream.write(output, message);
    }
}
