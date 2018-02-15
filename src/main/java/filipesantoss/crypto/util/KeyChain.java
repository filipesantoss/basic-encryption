package filipesantoss.crypto.util;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class KeyChain {

    private KeyPair pair;
    private Key symmetric;
    private Key foreign;

    public KeyChain(String algorithm, int keySize) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
        keyGen.initialize(keySize);
        pair = keyGen.generateKeyPair();
    }

    public void setForeign(Key foreign) {
        this.foreign = foreign;
    }

    public Key getPublic() {
        return pair.getPublic();
    }

    public void createSymmetric() throws NoSuchAlgorithmException {
        symmetric = KeyGenerator.getInstance(Values.SYMMETRIC_KEY_ALGORITHM).generateKey();
    }

    public Key getSymmetric() {
        return symmetric;
    }
}
