package org.janusvault.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.janusvault.crypto.CryptoService;
import org.janusvault.model.PasswordEntry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class StorageService {

    public static void save(List<PasswordEntry> entries, String masterKey, String filename)
            throws IOException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        JsonMapper mapper = new JsonMapper();
        String jsonData = mapper.writeValueAsString(entries);
        String jsonDataEncrypted = CryptoService.encrypt(jsonData, masterKey);

        Files.writeString(new File(filename).toPath(), jsonDataEncrypted);
    }

    public static List<PasswordEntry> load(String masterKey, String filename)
            throws IOException, NoSuchPaddingException,
            IllegalBlockSizeException, NoSuchAlgorithmException,
            BadPaddingException, InvalidKeyException {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<PasswordEntry>();
        }

        String jsonDataDecrypted = Files.readString(file.toPath());
        String jsonData = CryptoService.decrypt(jsonDataDecrypted, masterKey);

        JsonMapper mapper = new JsonMapper();
       return mapper.readValue(jsonData, new TypeReference<List<PasswordEntry>>() {});
    }

}
