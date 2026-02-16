package org.janusvault.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.janusvault.crypto.CryptoService;
import org.janusvault.model.PasswordEntry;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.janusvault.storage.ConfigService.APP_DIR;

public class StorageService {

    private static final JsonMapper MAPPER = new JsonMapper();

    public static void save(List<PasswordEntry> entries, char[] masterKey, String filename)
            throws Exception {
        byte[] rawJson = MAPPER.writeValueAsBytes(entries);
        byte[] encryptedJson = null;

        try {
            encryptedJson = CryptoService.encrypt(rawJson, masterKey);

            Files.write(Paths.get(filename), encryptedJson);
        } finally {
            Arrays.fill(rawJson, (byte) 0);
            if (encryptedJson != null)
                Arrays.fill(encryptedJson, (byte) 0);
        }
    }

    public static List<PasswordEntry> load(char[] masterKey, String filename)
            throws Exception {
        File file = new File(filename);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        byte[] encryptedJson = Files.readAllBytes(file.toPath());
        byte[] decryptedJson = null;

        try {
            decryptedJson = CryptoService.decrypt(encryptedJson, masterKey);

            return MAPPER.readValue(decryptedJson, new TypeReference<List<PasswordEntry>>() {
            });
        } finally {
            if (decryptedJson != null)
                Arrays.fill(decryptedJson, (byte) 0);
            Arrays.fill(encryptedJson, (byte) 0);
        }
    }

    public static List<String> getAvailableVaults() {
        try {
            if (!Files.exists(APP_DIR))
                return Collections.emptyList();

            try (Stream<Path> stream = Files.list(APP_DIR)) {
                return stream
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .filter(name -> name.endsWith(".db"))
                        .map(name -> name.replace(".db", ""))
                        .sorted()
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
