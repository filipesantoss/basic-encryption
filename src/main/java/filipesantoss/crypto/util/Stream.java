package filipesantoss.crypto.util;

import java.io.*;
import java.net.Socket;

/**
 * Utilitary class containing methods to read and write objects from and to object streams, as well as closing them.
 */
public class Stream {

    /**
     * Reads an object from the input stream.
     *
     * @param input - the input stream.
     * @param <T>   - the expected type of the read object.
     * @return the read object.
     * @see ObjectInputStream#readObject()
     */
    @SuppressWarnings("unchecked")
    public static <T> T read(ObjectInputStream input) {
        T object = null;

        try {
            object = (T) input.readObject();

        } catch (ClassNotFoundException e) {
            System.err.println("Failed to cast object.");
        } catch (EOFException e) {
            //reaching end of object is an expected behaviour
        } catch (IOException e) {
            System.err.println("Failed to read object.");
        }

        return object;
    }

    /**
     * Writes an object to the output stream.
     *
     * @param output - the output stream.
     * @param object - the object to write.
     * @see ObjectOutputStream#writeObject(Object)
     */
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

    /**
     * Closes a stream.
     *
     * @param stream - the stream to close.
     * @see Closeable#close()
     */
    public static void close(Closeable stream) {
        try {
            stream.close();

        } catch (IOException e) {
            System.err.println("Failed to close socket.");
        }
    }
}
