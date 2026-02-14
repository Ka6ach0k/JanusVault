package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine.*;

import java.util.List;

@Command(
        name = "add",
        description = "Добавляет пароль"
)
public class AddCommand implements Runnable {

    @Parameters(index = "0", description = "Название ресурса")
    private String title;

    @Option(names = {"-s", "--site"}, description = "Сайт", required = true)
    private String site;

    @Option(names = {"-l", "--login"}, description = "Логин", required = true)
    private String login;

    @Option(names = {"-p", "--pass"}, description = "Пароль", interactive = true, required = true, arity = "0..1")
    private String password;

    @Option(names = {"-n", "--note"}, description = "Описание")
    private String note = "";

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
            entries.add(new PasswordEntry(title, site, login, password, note));
            entries.sort(
                    (entry1, entry2) ->
                            entry1.getTitle().compareToIgnoreCase(entry2.getTitle())
            );
            StorageService.save(entries, masterKey, JanusCommand.DEFAULT_PATH);
            System.out.println("Успешно сохранено");
        } catch (Exception e) {
            System.err.println("Ошибка доступа. Проверьте мастер ключ");
        }
    }
}
