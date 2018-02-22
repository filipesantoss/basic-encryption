package filipesantoss.crypto.communication;

import filipesantoss.crypto.util.Constants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class KeyHandlerTest {

    private KeyHandler keyHandler;
    private String content;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        content = "content";

        keyHandler = new KeyHandler(Constants.KEYPAIR_ALGORITHM, Constants.KEYPAIR_KEY_SIZE);
    }

    @Test
    public void testValidCreateSymmetric() {
        keyHandler.createSymmetric();

        Assert.assertNotNull(keyHandler.getSymmetric());
    }

    @Test
    public void testEncryptWithSymmetric() {
        keyHandler.createSymmetric();

        Assert.assertNotNull(keyHandler.encryptWithSymmetric(content));
    }

    @Test
    public void testValidDecrypt() {
        try {
            keyHandler.createSymmetric();
            Key symmetric = keyHandler.getSymmetric();

            Cipher cipher = Cipher.getInstance(symmetric.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, symmetric);

            Message<String> message = keyHandler.encryptWithSymmetric(content);

            Assert.assertEquals(message.getContent(symmetric), content);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            System.err.println("Can't decrypt.");
        }
    }

    @Test
    public void testInvalidDecrypt() {
        try {
            keyHandler.createSymmetric();
            Key privateKey = keyHandler.getPrivate();

            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);

            Message<String> message = keyHandler.encryptWithSymmetric(content);

            Assert.assertNull(message.getContent(privateKey));
            Assert.assertEquals(message.getContent(keyHandler.getSymmetric()), content);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            System.err.println("Failed to create cipher: " + e.getMessage());
        }
    }
}