package janus.storage;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoad()
            throws Exception {
        Path vaultPath = tempDir.resolve("test.db");
        char[] key = "secret-key".toCharArray();

        List<PasswordEntry> savedEntries = new ArrayList<>();
        savedEntries.add(
                new PasswordEntry(
                        "Title".toCharArray(),
                        "Site".toCharArray(),
                        "Login".toCharArray(),
                        "Pass".toCharArray(),
                        "Note".toCharArray()
                ));

        StorageService.save(savedEntries, key, vaultPath.toString());
        List<PasswordEntry> loadedEntries = StorageService.load(key, vaultPath.toString());

        assertEquals(savedEntries.size(), loadedEntries.size());
        assertArrayEquals(savedEntries.get(0).getTitle(), loadedEntries.get(0).getTitle());
    }
}
