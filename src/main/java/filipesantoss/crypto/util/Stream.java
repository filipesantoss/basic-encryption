package filipesantoss.crypto.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Stream {

    public static <T> T read(ObjectInputStream input) {
        T object = null;

        try {
            object = (T) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Failed to read object.");
            e.printStackTrace();
        }

        return object;
    }

    public static void write(ObjectOutputStream output, Object object) {
        if (object == null) {
            return;
        }

        try {
            output.writeObject(object);
            output.flush();

        } catch (IOException e) {
            System.err.println("Failed to write object.");
            e.printStackTrace();
        }
    }

    public static void close(Socket socket) {
        try {
            socket.close();

        } catch (IOException e) {
            System.err.println("Failed to close socket.");
            e.printStackTrace();
        }
    }
}
