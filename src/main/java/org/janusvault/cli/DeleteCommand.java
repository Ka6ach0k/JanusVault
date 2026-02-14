package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine.*;

import java.util.List;

@Command(
        name = "delete",
        description = "Удаление записи для удаления"
)
public class DeleteCommand implements Runnable {

    @Parameters(index = "0", description = "Заголовок записи для удаления")
    private String title;

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
            List<PasswordEntry> entries = StorageService.load(masterKey,JanusCommand.DEFAULT_PATH);

            boolean removed = entries.removeIf(entry -> entry.getTitle().equalsIgnoreCase(title));

            if (removed) {
                StorageService.save(entries, masterKey, JanusCommand.DEFAULT_PATH);
                System.out.println("Запись успешна удалено");
            } else {
                System.out.println("Запись с таким заголовком не найдена");
            }
        } catch (Exception e) {
            System.err.println("Ошибка. Проверьте мастер ключ");
        }
    }
}
