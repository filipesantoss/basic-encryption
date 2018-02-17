package filipesantoss.crypto.util;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class Message<T> extends SealedObject {

    public Message(Serializable message, Cipher cipher) throws IOException, IllegalBlockSizeException {
        super(message, cipher);
    }

    public T getContent(Key key) {
        try {
            Object object = getObject(key);
            return (T) object;

        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }
}