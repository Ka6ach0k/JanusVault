package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine.*;

import java.util.ArrayList;
import java.util.List;

@Command(
        name = "update",
        description = "Обновить существующую запись"
)
public class UpdateCommand implements Runnable {

    @Parameters(index = "0", description = "Заголовок записи, которую нужно изменить")
    private String title;

    @Option(names = {"-nt", "--new-title"}, description = "Новый заголовок")
    private String newTitle;

    @Option(names = {"-s", "--site"}, description = "Новый сайт")
    private String newSite;

    @Option(names = {"-l", "--login"}, description = "Новый логин")
    private String newLogin;

    @Option(names = {"-p", "--pass"}, description = "Новый пароль", interactive = true, arity = "0..1")
    private String newPassword;

    @Option(names = {"-m", "--master"}, description = "Мастер-пароль", interactive = true, required = true)
    private String masterKey;

    @Override
    public void run() {
        try {
            List<PasswordEntry> entries = StorageService.load(masterKey, JanusCommand.DEFAULT_PATH);

            PasswordEntry foundRecord = entries.stream()
                    .filter(entry -> entry.getTitle().equalsIgnoreCase(title))
                    .findFirst()
                    .orElse(null);

            if (foundRecord != null) {
                if (newTitle != null && !newTitle.isBlank()) foundRecord.setTitle(newTitle);
                if (newSite != null) foundRecord.setSite(newSite);
                if (newLogin != null) foundRecord.setLogin(newLogin);
                if (newPassword != null) foundRecord.setPassword(newPassword);

                StorageService.save(entries, masterKey, JanusCommand.DEFAULT_PATH);
                System.out.println("Запись успешно обновлена");
            } else {
                System.out.println("Запись с таким заголовком не найдена");
            }
        } catch (Exception e) {
            System.err.println("Ошибка обновления");
        }

    }
}
