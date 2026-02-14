package janusvault.storage;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageServiceTest {

    @TempDir
    Path tempDir;

    @Test
    void testSaveAndLoad() throws Exception {
        String filename = tempDir.resolve("testJsonBase").toString();
        String masterKey = "master-key-for-testing";

        List<PasswordEntry> entriesSaved =
                List.of(new PasswordEntry("Title", "Site", "Login", "Password", "Note"));

        StorageService.save(entriesSaved, masterKey, filename);

        List<PasswordEntry> entriesLoaded = StorageService.load(masterKey, filename);

        assertEquals(1, entriesLoaded.size());
        assertEquals(entriesSaved.get(0), entriesLoaded.get(0));
        assertEquals("Password", entriesLoaded.get(0).getPassword());
    }
}
