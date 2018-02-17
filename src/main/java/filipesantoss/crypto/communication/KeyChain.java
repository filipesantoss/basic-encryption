package filipesantoss.crypto.communication;

import filipesantoss.crypto.util.Constants;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.Serializable;
import java.security.*;

public class KeyChain {

    private final KeyPair pair;
    private Key symmetric;
    private Key foreign;

    public KeyChain(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        pair = keyGen.generateKeyPair();
    }

    public void createSymmetric() {
        try {
            symmetric = KeyGenerator.getInstance(Constants.SYMMETRIC_KEY_ALGORITHM).generateKey();

        } catch (NoSuchAlgorithmException e) {
            System.err.println("Failed to generate symmetric key.");
            e.printStackTrace();
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
            e.printStackTrace();

        }

        return message;
    }

    public <T> Message<T> encryptWithForeign(Serializable content) {
        return encrypt(content, foreign);
    }

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
