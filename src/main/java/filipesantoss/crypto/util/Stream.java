package filipesantoss.crypto.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Stream {

    public static <T> T read(ObjectInputStream input) {
        T object = null;

        try {
            object = (T) input.readObject();

        } catch (IOException | ClassNotFoundException e) {
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
            e.printStackTrace();
        }
    }
}
