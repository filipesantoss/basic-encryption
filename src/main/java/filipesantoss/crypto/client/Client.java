package filipesantoss.crypto.client;

import filipesantoss.crypto.communication.KeyChain;
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
    private KeyChain keyChain;

    public Client() throws IOException {
        socket = new Socket(Constants.HOST, Constants.PORT);
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
        keyChain = new KeyChain(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        exchangePublicKeys();
        receiveSymmetricKey();
        System.out.println("CONNECTED.");
        new Thread(new ScannerHandler(output, socket, keyChain)).start();
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

            String text = message.getContent(keyChain.getSymmetric());
            System.out.println(text);
        }

        stop();
    }


    private void exchangePublicKeys() {
        Key foreign = Stream.read(input);
        keyChain.setForeign(foreign);

        Stream.write(output, keyChain.getPublic());
    }

    private void receiveSymmetricKey() {
        Message<Key> sealedSymmetric = Stream.read(input);

        Key symmetric = sealedSymmetric.getContent(keyChain.getPrivate());
        keyChain.setSymmetric(symmetric);
    }

    /**
     * Closes the socket.
     */
    public void stop() {
        Stream.close(socket);
    }
}
