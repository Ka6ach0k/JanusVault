package janus.storage;

import org.janusvault.storage.ConfigService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConfigServiceTest {

    @Test
    void testResolveShorName() {
        String shorName = ConfigService.resolvePath("test");
        assertTrue(shorName.endsWith(".janusvault/test.db"));
    }

    @Test
    void testResolvePathAbsoluteName() {
        String absoluteName = "tmp/test.db";
        String resolved = ConfigService.resolvePath(absoluteName);
        assertEquals(absoluteName, resolved);
    }
}
