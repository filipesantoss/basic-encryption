package filipesantoss.crypto.server;

import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Constants;
import filipesantoss.crypto.util.Stream;

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
            System.out.println("client connected");
            pool.submit(new ClientHandler(client, keyChain.getSymmetric(), this));
        }
    }

    public void add(ClientHandler client) {
        clients.add(client);
    }

    public void remove(ClientHandler client) {
        clients.remove(client);
    }

    public void broadcast(ClientHandler client, Message message) {
        for (ClientHandler handler : clients) {

            if (handler == client) {
                System.out.println("same");
                continue;
            }

            handler.write(message);
        }
    }

    public void stop() {
        Stream.close(socket);
    }
}