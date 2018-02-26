package filipesantoss.crypto.client;

import filipesantoss.crypto.communication.KeyHandler;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;
import filipesantoss.crypto.util.Constants;

import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * This class is responsible for establishing a connection to the server,
 * exchanging keys with it and reading from it.
 */
public class Client {

    private final Socket socket;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private KeyHandler keyHandler;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    /**
     * Receives the server's public key and sends its own public key.
     * Receives the server's symmetric key.
     *
     * @throws NoSuchAlgorithmException if there's no supported implementation of the algorithm
     *                                  used to generate the public key.
     */
    public void init() throws NoSuchAlgorithmException {
        keyHandler = new KeyHandler(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        exchangePublicKeys();
        receiveSymmetricKey();
        System.out.println("CONNECTED.");
        new Thread(new ScannerHandler(output, socket, keyHandler)).start();
    }

    /**
     * While there's an object to read through the input stream, reads it, decrypts it and prints it.
     *
     * @see Message#getContent(Key)
     */
    public void start() {
        while (true) {
            Message<String> message = Stream.read(input);

            if (message == null) {
                break;
            }

            String text = message.getContent(keyHandler.getSymmetric());
            System.out.println(text);
        }

        stop();
    }


    private void exchangePublicKeys() {
        Key foreign = Stream.read(input);
        keyHandler.setForeign(foreign);

        Stream.write(output, keyHandler.getPublic());
    }

    private void receiveSymmetricKey() {
        Message<Key> sealedSymmetric = Stream.read(input);

        Key symmetric = sealedSymmetric.getContent(keyHandler.getPrivate());
        keyHandler.setSymmetric(symmetric);
    }

    /**
     * Closes the socket.
     */
    public void stop() {
        Stream.close(socket);
    }
}
