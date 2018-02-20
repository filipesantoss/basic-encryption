package filipesantoss.crypto.communication;

import filipesantoss.crypto.util.Constants;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.*;

/**
 * Wrapper class to encapsulate Key related behavior.
 */
public class KeyChain {

    private final KeyPair pair;
    private Key symmetric;
    private Key foreign;

    /**
     * Initializes a KeyPair generated from the specified algorithm and with the specified key size.
     *
     * @param algorithm - the algorithm used to generate the KeyPair.
     * @param keySize   - the key size used by the KeyPairGenerator.
     * @throws NoSuchAlgorithmException if there's no supported implementation of the specified algorithm.
     * @see KeyPairGenerator#generateKeyPair()
     */
    public KeyChain(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(algorithm);
        generator.initialize(keySize);
        pair = generator.generateKeyPair();
    }

    /**
     * Generates a symmetric key.
     *
     * @see KeyGenerator#generateKey()
     */
    public void createSymmetric() {
        try {
            symmetric = KeyGenerator.getInstance(Constants.SYMMETRIC_KEY_ALGORITHM).generateKey();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to generate symmetric key.");
        }
    }

    private <T> Message<T> encrypt(Serializable content, Key key) {
        Message<T> message = null;

        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            message = new Message<>(content, cipher);

        } catch (IOException | IllegalBlockSizeException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidKeyException e) {
            System.err.println("Failed to encrypt " + content);
        }

        return message;
    }

    /**
     * Returns a Message with the specified content and the cipher initialized with the foreign key.
     *
     * @param content - the content of the message.
     * @param <T>     - the expected return type.
     * @return the instantiated message.
     * @see Cipher#init(int, Key)
     * @see Message
     */
    public <T> Message<T> encryptWithForeign(Serializable content) {
        return encrypt(content, foreign);
    }

    /**
     * Returns a Message with the specified content and the cipher initialized with the symmetric key.
     *
     * @param content - the content of the message.
     * @param <T>     - the expected return type.
     * @return the instantiated message.
     * @see Cipher#init(int, Key)
     * @see Message
     */
    public <T> Message<T> encryptWithSymmetric(Serializable content) {
        return encrypt(content, symmetric);
    }

    public Key getSymmetric() {
        return symmetric;
    }

    public Key getPrivate() {
        return pair.getPrivate();
    }

    public Key getPublic() {
        return pair.getPublic();
    }

    public void setSymmetric(Key symmetric) {
        this.symmetric = symmetric;
    }

    public void setForeign(Key foreign) {
        this.foreign = foreign;
    }
}
