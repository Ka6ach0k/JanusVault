package janusvault.crypto;

import org.janusvault.crypto.CryptoService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CryptoServiceTest {

    @Test
    void testEncryptDecrypt()  throws Exception {
        String masterKey = "my-test-key-for-encryption";
        String originalPassword = "my-password-for-testing";

        String encryptedPassword = CryptoService.encrypt(originalPassword, masterKey);

        String decryptedPassword = CryptoService.decrypt(encryptedPassword, masterKey);

        assertEquals(originalPassword, decryptedPassword, "Данные после шифрования должны совпадать");
    }

    @Test
    void testWrongKey()  throws Exception {
        String correctMasterKey = "correct-key";
        String wrongMasterKey = "wrong-key";
        String originalPassword = "my-password-for-testing";

        String encryptedPassword = CryptoService.encrypt(originalPassword, correctMasterKey);

        assertThrows(Exception.class, () -> {
            CryptoService.decrypt(encryptedPassword, wrongMasterKey);
        });
    }
}
