package filipesantoss.crypto.util;

public class Constants {

    /**
     * The algorithm to be used when generating a KeyPair.
     *
     * @see java.security.KeyPairGenerator#getInstance(String)
     */
    public static final String KEYPAIR_ALGORITHM = "RSA";

    /**
     * The key size to be used when generating a KeyPair.
     *
     * @see java.security.KeyPairGenerator#initialize(int)
     */
    public static final int KEYPAIR_KEY_SIZE = 4096;

    /**
     * The algorithm to be used when generating the symmetric key.
     *
     * @see javax.crypto.KeyGenerator#getInstance(String)
     */
    public static final String SYMMETRIC_KEY_ALGORITHM = "DES";
}
