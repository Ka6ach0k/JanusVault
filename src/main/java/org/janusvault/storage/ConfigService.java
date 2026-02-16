package org.janusvault.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigService {
    public static final Path APP_DIR =
            Paths.get(System.getProperty("user.home"), ".janusvault");
    public static final Path CONFIG_PATH =
            APP_DIR.resolve("config.properties");
    public static final String DEFAULT_VAULT_TAG = "default.vault";

    static {
        try {
            Files.createDirectories(APP_DIR);
        } catch (Exception ignored) {
        }
    }

    public static String resolvePath(String path) {
        if (path == null || path.isEmpty())
            path = getDefaultVaultTag();

        boolean isShortName = !path.contains(".") && !path.contains("/") && !path.contains("\\");
        if (isShortName) {
            return APP_DIR.resolve(path + ".db").toAbsolutePath().toString();
        }

        return Paths.get(path).toString();
    }

    public static String getDefaultVaultTag() {
        Properties properties = new Properties();
        try (InputStream in = new FileInputStream(CONFIG_PATH.toFile())) {
            properties.load(in);
            return properties.getProperty(DEFAULT_VAULT_TAG, "main");
        } catch (Exception ignored) {
            return "main";
        }
    }

    public static void setDefaultVault(String nameVault)
            throws Exception {
        Properties properties = new Properties();

        File file = CONFIG_PATH.toFile();
        if (file.exists())
            try (InputStream in = new FileInputStream(file)) {
                properties.load(in);
            }

        properties.setProperty(DEFAULT_VAULT_TAG, nameVault);
        try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_PATH)) {
            writer.write("# JanusVault Config");
            writer.newLine();
            for (String key : properties.stringPropertyNames()) {
                writer.write(key + "=" + properties.getProperty(key));
                writer.newLine();
            }
        }
    }
}
