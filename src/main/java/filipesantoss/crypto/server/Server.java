package filipesantoss.crypto.server;

import filipesantoss.crypto.util.KeyChain;
import filipesantoss.crypto.util.Values;

import java.io.IOException;
import java.net.ServerSocket;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket socket;
    private Key symmetric;

    public void init() throws IOException, NoSuchAlgorithmException {

        socket = new ServerSocket(Values.PORT);

        KeyChain keyChain = new KeyChain(Values.KEYPAIR_ALGORITHM, Values.KEYPAIR_KEY_SIZE);
        keyChain.createSymmetric();
        symmetric = keyChain.getSymmetric();
    }
}
