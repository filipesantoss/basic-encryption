package filipesantoss.crypto.server;

import filipesantoss.crypto.communication.KeyHandler;
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

    /**
     * Generates a symmetric key.
     * Accepts TCP connections from clients and launches a thread to communicate with it.
     *
     * @throws NoSuchAlgorithmException if there's no supported implementation of the algorithm
     *                                  used to generate the symmetric key.
     * @throws IOException              if the connection can't be established.
     * @see KeyHandler#createSymmetric()
     */
    public void start() throws NoSuchAlgorithmException, IOException {
        KeyHandler keyHandler = new KeyHandler(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
        keyHandler.createSymmetric();

        ExecutorService pool = Executors.newCachedThreadPool();

        while (true) {
            Socket client = socket.accept();
            System.out.println("client connected");
            pool.submit(new ClientHandler(client, keyHandler.getSymmetric(), this));
        }
    }

    /**
     * Adds a client to the server.
     *
     * @param client - the client to be added.
     */
    public void add(ClientHandler client) {
        clients.add(client);
    }

    /**
     * Removes a client from the server.
     *
     * @param client - the client to be removed.
     */
    public void remove(ClientHandler client) {
        clients.remove(client);
    }

    /**
     * Writes a message to every client except the one sending it.
     *
     * @param client  - the client sending the message.
     * @param message - the message to send.
     * @see ClientHandler#write(Message)
     */
    public void broadcast(ClientHandler client, Message message) {
        for (ClientHandler handler : clients) {

            if (handler == client) {
                continue;
            }

            handler.write(message);
        }
    }

    /**
     * Closes the ServerSocket.
     */
    public void stop() {
        Stream.close(socket);
    }
}