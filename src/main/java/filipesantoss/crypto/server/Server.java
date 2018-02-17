package filipesantoss.crypto.server;

import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.util.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private final ServerSocket socket;
    private final CopyOnWriteArrayList<ClientHandler> clients;

    public Server() throws IOException {
        socket = new ServerSocket(Constants.PORT);
        clients = new CopyOnWriteArrayList<>();
    }

    public void start() throws NoSuchAlgorithmException, IOException {

        KeyChain keyChain = new KeyChain(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        keyChain.createSymmetric();

        ExecutorService pool = Executors.newCachedThreadPool();

        while (true) {
            Socket client = socket.accept();
            pool.submit(new ClientHandler(client, keyChain.getSymmetric(), this));
        }
    }

    public void add(ClientHandler client) {
        clients.add(client);
    }
}
