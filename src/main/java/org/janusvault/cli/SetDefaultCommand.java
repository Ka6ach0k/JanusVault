package org.janusvault.cli;

import org.janusvault.storage.ConfigService;
import org.janusvault.util.PrintMessage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Command(
        name = "set-default",
        mixinStandardHelpOptions = true,
        description = "Устанавливает хранилище по умолчанию"
)
public class SetDefaultCommand implements Runnable {

    @Parameters(index = "0")
    private String vaultName;

    @Override
    public void run() {
        try {
            String resolvePathStr = ConfigService.resolvePath(vaultName);
            Path resolvePath = Paths.get(resolvePathStr).toAbsolutePath();

            if (!Files.exists(resolvePath)) {
                PrintMessage.printError("Хранилище не найдено");
                return;
            }

            Path appDir = ConfigService.APP_DIR.toAbsolutePath();

            if (!resolvePath.startsWith(appDir)) {
                PrintMessage.printError("Это хранилище нельзя установить по умолчанию");
                return;
            }


            ConfigService.setDefaultVault(vaultName);
            PrintMessage.printSuccess("Хранилище установлено по умолчанию");
        } catch (Exception e) {
            PrintMessage.printError("Не удалось установить по умолчанию");
        }
    }
}
