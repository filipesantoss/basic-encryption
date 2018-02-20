package filipesantoss.crypto.client;

import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;

import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class receives input from the terminal and writes an object to an output stream.
 */
public class ScannerHandler implements Runnable {

    private final ObjectOutputStream output;
    private final KeyChain keyChain;
    private final Socket socket;

    public ScannerHandler(ObjectOutputStream output, Socket socket, KeyChain keyChain) {
        this.output = output;
        this.keyChain = keyChain;
        this.socket = socket;
    }

    /**
     * Instantiates a Scanner to read input from the terminal.
     * While the socket is not closed, reads a line through it, encrypts it
     * and writes an object containing the encrypted version to the output stream.
     *
     * @see Scanner
     * @see KeyChain#encryptWithSymmetric(Serializable)
     * @see Stream#write(ObjectOutputStream, Object)
     */
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String text = scanner.nextLine();

            if (socket.isClosed()) {
                System.err.println("Closing client...");
                break;
            }

            Message<String> message = keyChain.encryptWithSymmetric(text);
            Stream.write(output, message);
        }

        Stream.close(scanner);
    }
}