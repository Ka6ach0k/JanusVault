package org.janusvault.cli;

import org.janusvault.storage.ConfigService;
import org.janusvault.storage.StorageService;
import org.janusvault.util.PrintMessage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

@Command(
        name = "init",
        mixinStandardHelpOptions = true,
        description = "Создать новое хранилище"
)
public class InitCommand implements Runnable {

    @ParentCommand
    private JanusCommand parent;

    @Parameters(index = "0", description = "Имя нового хранилища")
    private String name;

    @Option(
            names = {"-m", "--master"},
            description = "Задайте мастер ключ",
            interactive = true,
            required = true
    )
    private char[] masterKey;

    @Option(
            names = {"-d", "--default"},
            description = "Сделать это хранилище по умолчанию"
    )
    private boolean makeDefaultVault;

    public void run() {
        try {
            String vaultPath = ConfigService.resolvePath(name);

            File file = new File(vaultPath);

            if (file.exists()) {
                PrintMessage.printError("Хранилище уже существует");
                return;
            }
            StorageService.save(new ArrayList<>(), masterKey, vaultPath);

            if (makeDefaultVault)
                ConfigService.setDefaultVault(name);

            PrintMessage.printSuccess("Хранилище создано");
        } catch (Exception e) {
            PrintMessage.printError("Ошибка при создания хранилища");
        } finally {
            if (masterKey != null)
                Arrays.fill(masterKey, '\0');
        }
    }
}
