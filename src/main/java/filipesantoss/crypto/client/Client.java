package filipesantoss.crypto.client;

import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;
import filipesantoss.crypto.util.Constants;

import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Client {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private KeyChain keyChain;

    public Client() throws IOException {
        socket = new Socket(Constants.HOST, Constants.PORT);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
    }

    public void init() throws NoSuchAlgorithmException {
        keyChain = new KeyChain(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        exchangePublicKeys();
        receiveSymmetricKey();
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
}
