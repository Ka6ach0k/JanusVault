package org.janusvault.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class CryptoService {
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHM = "AES";

    public static String encrypt(String data, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = generateSecretKey(key);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        byte[] dataEncrypted = cipher.doFinal(data.getBytes());

        return Base64.getEncoder().encodeToString(dataEncrypted);
    }

    public static String decrypt(String data, String key)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        SecretKeySpec secretKeySpec = generateSecretKey(key);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] dataDecrypted = cipher.doFinal(Base64.getDecoder().decode(data));

        return new String(dataDecrypted);
    }

    public static SecretKeySpec generateSecretKey(String key)
            throws NoSuchAlgorithmException {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(keyBytes);

        return new SecretKeySpec(digest, ALGORITHM);
    }
}
