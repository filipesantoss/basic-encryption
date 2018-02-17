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

    private KeyPair pair;
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

    private <T> Message<T> cipher(Serializable content, Key key) {
        Message<T> message = null;

        try {
            Cipher cipher = Cipher.getInstance(key.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            message = new Message<>(content, cipher);

        } catch (IOException | IllegalBlockSizeException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidKeyException e) {
            System.err.println("Failed to cipher " + content);
            e.printStackTrace();

        }

        return message;
    }

    public <T> Message<T> cipherWithForeign(Serializable content) {
        return cipher(content, foreign);
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
