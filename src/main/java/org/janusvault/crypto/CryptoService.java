package org.janusvault.crypto;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Arrays;

public class CryptoService {
    private static final String TRANSFORMATION = "AES/GСM/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final String FACTORY_ALGORITHM = "PBKDF2WithHmacSHA256";

    private static final int TAG_LENGTH_BITS = 128;
    private static final int IV_LENGTH_BITS = 12;
    private static final int SALT_LENGTH_BITS = 12;
    private static final int ITERATIONS_COUNT = 65536;
    private static final int KEY_LENGTH_BITS = 256;

    public static byte[] encrypt(byte[] data, char[] key)
            throws Exception {

        byte[] salt = generateRandomBytes(SALT_LENGTH_BITS);
        byte[] iv = generateRandomBytes(IV_LENGTH_BITS);
        byte[] encryptedData = null;

        SecretKey secretKey = generateKey(key, salt);

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(
                    Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

            encryptedData = cipher.doFinal(data);

            ByteBuffer buffer = ByteBuffer.allocate(salt.length + iv.length + encryptedData.length);
            buffer.put(salt);
            buffer.put(iv);
            buffer.put(encryptedData);

            return buffer.array();
        } finally {
            Arrays.fill(salt, (byte) 0);
            Arrays.fill(iv, (byte) 0);
            if (encryptedData != null)
                Arrays.fill(encryptedData, (byte) 0);
        }
    }

    public static byte[] decrypt(byte[] encryptedData, char[] key)
            throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(encryptedData);

        byte[] salt = new byte[SALT_LENGTH_BITS];
        buffer.get(salt);
        byte[] iv = new byte[IV_LENGTH_BITS];
        buffer.get(iv);
        byte[] data = new byte[buffer.remaining()];
        buffer.get(data);

        SecretKey secretKey = generateKey(key, salt);

        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(TAG_LENGTH_BITS, iv));

            return cipher.doFinal(data);
        } finally {
            Arrays.fill(salt, (byte) 0);
            Arrays.fill(iv, (byte) 0);
            Arrays.fill(data, (byte) 0);
        }
    }

    private static SecretKey generateKey(char[] key, byte[] salt)
            throws Exception {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(FACTORY_ALGORITHM);
        PBEKeySpec spec = new PBEKeySpec(key, salt, ITERATIONS_COUNT, KEY_LENGTH_BITS);

        try {
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
        } finally {
            spec.clearPassword();
        }
    }

    private static byte[] generateRandomBytes(int length) {
        byte[] result = new byte[length];
        new SecureRandom().nextBytes(result);
        return result;
    }
}