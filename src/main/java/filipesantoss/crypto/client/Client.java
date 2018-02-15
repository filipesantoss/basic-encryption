package filipesantoss.crypto.client;

import filipesantoss.crypto.util.KeyChain;
import filipesantoss.crypto.util.Stream;
import filipesantoss.crypto.util.Values;

import java.io.*;
import java.net.Socket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Client {

    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private KeyChain keyChain;

    public void connect() throws IOException, NoSuchAlgorithmException {
        socket = new Socket(Values.HOST, Values.PORT);
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        keyChain = new KeyChain(Values.KEYPAIR_ALGORITHM, Values.KEYPAIR_KEY_SIZE);
        exchangePublicKeys();
    }

    private void exchangePublicKeys() {

        Key foreign = Stream.read(input);
        keyChain.setForeign(foreign);
        Stream.write(output, keyChain.getPublic());
    }
}
