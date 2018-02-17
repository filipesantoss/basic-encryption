package filipesantoss.crypto.util;

import java.io.*;
import java.net.Socket;

public class Stream {

    public static <T> T read(ObjectInputStream input) {
        T object = null;

        try {
            object = (T) input.readObject();

        } catch (ClassNotFoundException e) {
            System.err.println("Failed to cast object.");
        }

        catch (EOFException e) {
            //TODO reaching end of object is an expected behaviour. fix?
        }

        catch (IOException e) {
            System.err.println("Failed to read object.");
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
        }
    }

    public static void close(Closeable socket) {
        try {
            socket.close();

        } catch (IOException e) {
            System.err.println("Failed to close socket.");
        }
    }
}
