package org.janusvault.cli;

import org.janusvault.storage.ConfigService;
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
                String message = "Хранилище не найдено";
                System.err.println(
                        CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@"));
                return;
            }

            Path appDir = ConfigService.APP_DIR.toAbsolutePath();

            if (!resolvePath.startsWith(appDir)) {
                String message = "Это хранилище нельзя установить по умолчанию";
                System.err.println(
                        CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@"));
                return;
            }


            ConfigService.setDefaultVault(vaultName);
            String message = "Хранилище установлено по умолчанию";
            System.out.println(
                    CommandLine.Help.Ansi.AUTO.string("@|green " + message + " |@"));
        } catch (Exception e) {
            String message = "Не удалось установить по умолчанию";
            System.err.println(
                    CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@"));
        }
    }
}
