package filipesantoss.crypto.communication;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SealedObject;
import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

/**
 * A parameterizable wrapper to the SealedObject class.
 *
 * @param <T> - the content type.
 * @see SealedObject
 */
public class Message<T> extends SealedObject {

    Message(Serializable content, Cipher cipher) throws IOException, IllegalBlockSizeException {
        super(content, cipher);
    }

    /**
     * Returns the unsealed content of the object using the specified key.
     *
     * @param key - the key to unseal the object
     * @return the unsealed object
     * @see SealedObject#getObject(Key)
     */
    @SuppressWarnings("unchecked")
    public T getContent(Key key) {
        try {
            Object object = getObject(key);
            return (T) object;

        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | InvalidKeyException e) {
            System.err.println("Failed to retrieve the object.");
        }

        return null;
    }
}