package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.List;

@Command(
        name = "list",
        description = "Показывает все записи"
)
public class ListCommand implements Runnable {

    @Option(
            names = {"-m", "--master"},
            description = "Мастер ключ",
            interactive = true,
            required = true
    )
    private String masterKey;

    @Override
    public void run() {
        try {
            List<PasswordEntry> entries = StorageService.load(masterKey, JanusCommand.DEFAULT_PATH);
            System.out.println();
            System.out.println("--- Ваши Пароли ---");
            for (PasswordEntry entry : entries) {
                System.out.println(entry);
            }
        } catch (Exception e) {
            System.err.println("Не удалось прочитать базу паролей. Проверьте мастер ключ");
        }

    }
}
