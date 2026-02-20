package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import org.janusvault.util.CharUtil;
import org.janusvault.util.PrintMessage;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Command(
        name = "update",
        aliases = {"edit"},
        mixinStandardHelpOptions = true,
        description = "Обновляет существующую запись"
)
public class UpdateCommand implements Runnable {

    @ParentCommand
    private JanusCommand parent;

    @Parameters(index = "0", description = "Название ресурса, которое нужно изменить")
    private char[] targetTitle;

    @Option(names = {"-s", "--site"}, description = "Новый сайт")
    private char[] newSite;

    @Option(names = {"-l", "--login"}, description = "Новый логин", interactive = true, arity = "0..1")
    private char[] newLogin;

    @Option(names = {"-p", "--pass"}, description = "Новый пароль", interactive = true, arity = "0..1")
    private char[] newPassword;

    @Option(names = {"-n", "--note"}, description = "Новое описание")
    private char[] newNote;

    @Option(
            names = {"-m", "--master"},
            description = "Мастер ключ",
            interactive = true,
            required = true
    )
    private char[] masterKey;

    @Override
    public void run() {
        updateRecord();
    }

    private void updateRecord() {
        List<PasswordEntry> entries = null;

        try {
            entries = StorageService.load(masterKey, parent.getVaultFilename());

            Optional<PasswordEntry> entry = entries.stream()
                    .filter(el -> CharUtil.equalsIgnoreCase(el.getTitle(), targetTitle))
                    .findFirst();

            if (entry.isEmpty()) {
                PrintMessage.printWarning("Запись не найдена");
                return;
            }

            if (newSite != null && newSite.length > 0)
                entry.get().setSite(newSite);
            if (newLogin != null && newLogin.length > 0)
                entry.get().setLogin(newLogin);
            if (newPassword != null && newPassword.length > 0)
                entry.get().setPassword(newPassword);
            if (newNote != null && newNote.length > 0)
                entry.get().setNote(newNote);

            StorageService.save(entries, masterKey, parent.getVaultFilename());
            PrintMessage.printSuccess("Запись успешно обновлена");
        } catch (Exception e) {
            PrintMessage.printError("Ошибка доступа. Проверьте мастер ключ");
        } finally {
            if (entries != null)
                entries.forEach(PasswordEntry::clean);
            if (masterKey != null) Arrays.fill(masterKey, '\0');
            if (targetTitle != null) Arrays.fill(targetTitle, '\0');
            if (newSite != null) Arrays.fill(newSite, '\0');
            if (newLogin != null) Arrays.fill(newLogin, '\0');
            if (newPassword != null) Arrays.fill(newPassword, '\0');
            if (newNote != null) Arrays.fill(newNote, '\0');
        }
    }
}
