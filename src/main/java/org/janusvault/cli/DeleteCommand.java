package org.janusvault.cli;


import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import org.janusvault.util.CharUtil;
import org.janusvault.util.PrintMessage;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.Arrays;
import java.util.List;

@Command(
        name = "delete",
        aliases = "{rm}",
        description = "Удаляет существующую запись"
)
public class DeleteCommand implements Runnable {

    @ParentCommand
    private JanusCommand parent;

    @Parameters(index = "0", description = "Заголовок записи")
    private char[] targetTitle;

    @Option(
            names = {"-m", "--master"},
            interactive = true,
            required = true,
            description = "Мастер ключ")
    private char[] masterKey;

    @Override
    public void run() {
        List<PasswordEntry> entries = null;
        try {
            entries = StorageService.load(masterKey, parent.getVaultFilename());

            if (entries.removeIf(
                    el -> CharUtil.equalsIgnoreCase(el.getTitle(), targetTitle))) {
                StorageService.save(entries, masterKey, parent.getVaultFilename());
                PrintMessage.printSuccess("Запись удалена");
            } else
                PrintMessage.printWarning("Запись не найдена");

        } catch (Exception e) {
            PrintMessage.printError("Ошибка доступа");
        } finally {
            if (entries != null) entries.forEach(PasswordEntry::clean);
            if (masterKey != null) Arrays.fill(masterKey, '\0');
        }
    }
}
