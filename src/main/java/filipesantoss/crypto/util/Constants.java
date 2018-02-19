package filipesantoss.crypto.util;

public class Constants {

    /**
     * The server host name.
     *
     * @see java.net.Socket#Socket(String, int)
     */
    public static final String HOST = "localhost";

    /**
     * The server port number.
     *
     * @see java.net.Socket#Socket(String, int)
     * @see java.net.ServerSocket#ServerSocket(int)
     */
    public static final int PORT = 8880;

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
