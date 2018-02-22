package filipesantoss.crypto.server;

import filipesantoss.crypto.util.Constants;
import filipesantoss.crypto.communication.KeyHandler;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class ClientHandler implements Runnable {

    private final Socket client;
    private final Server server;
    private final KeyHandler keyHandler;
    private ObjectOutputStream output;

    public ClientHandler(Socket client, Key symmetric, Server server) throws NoSuchAlgorithmException {
        this.client = client;
        keyHandler = new KeyHandler(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        keyHandler.setSymmetric(symmetric);
        this.server = server;
    }

    /**
     * Writes the public key to the client and reads the client's public key.
     * Writes an object containing the encrypted symmetric key to the client.
     * While there's an object to read through the input stream, reads it and broadcasts it.
     *
     * @see Stream
     * @see KeyHandler#encryptWithForeign(Serializable)
     * @see Server#broadcast(ClientHandler, Message)
     */
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
        Stream.write(output, keyHandler.getPublic());

        Key foreign = Stream.read(input);
        keyHandler.setForeign(foreign);
    }

    private void sendSymmetricKey(ObjectOutputStream output) {
        Message<Key> sealedSymmetric = keyHandler.encryptWithForeign(keyHandler.getSymmetric());

        Stream.write(output, sealedSymmetric);
    }

    /**
     * Writes a message to the client.
     *
     * @param message - the message to write.
     */
    public void write(Message message) {
        Stream.write(output, message);
    }
}
