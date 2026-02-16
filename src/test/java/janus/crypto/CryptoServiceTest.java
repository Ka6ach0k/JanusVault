package janus.crypto;

import org.janusvault.crypto.CryptoService;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoServiceTest {

    @Test
    void testEncryptAndDecrypt()
            throws Exception {
        char[] key = "secret-key".toCharArray();
        byte[] originalData = "secret-data".getBytes();

        byte[] encryptedData = CryptoService.encrypt(originalData, key);
        byte[] decryptedData = CryptoService.decrypt(encryptedData, key);

        assertArrayEquals(originalData, decryptedData);
        assertNotEquals(Arrays.hashCode(originalData), Arrays.hashCode(encryptedData));
    }

    @Test
    void testEncryptAndDecryptWrongKey()
            throws Exception {
        char[] correctKey = "correct-key".toCharArray();
        char[] wrongKey = "wrong-key".toCharArray();
        byte[] data = "data".getBytes();

        assertThrows(Exception.class, () -> {
            byte[] encryptedData = CryptoService.encrypt(data, correctKey);
            CryptoService.decrypt(encryptedData, wrongKey);
        });
    }
}
