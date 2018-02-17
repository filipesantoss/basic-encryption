package filipesantoss.crypto.client;

import filipesantoss.crypto.communication.KeyChain;
import filipesantoss.crypto.communication.Message;
import filipesantoss.crypto.util.Stream;

import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ScannerHandler implements Runnable {

    private final ObjectOutputStream output;
    private final KeyChain keyChain;
    private final Socket socket;

    public ScannerHandler(ObjectOutputStream output, Socket socket, KeyChain keyChain) {
        this.output = output;
        this.keyChain = keyChain;
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String text = scanner.nextLine();

            if (socket.isClosed()) {
                System.err.println("Server seems to be down. Closing client...");
                break;
            }

            Message<String> message = keyChain.encryptWithSymmetric(text);
            Stream.write(output, message);
        }

        Stream.close(scanner);
    }
}