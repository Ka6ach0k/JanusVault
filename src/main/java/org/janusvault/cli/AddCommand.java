package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.util.List;

import static org.janusvault.util.CharUtil.compareToIgnoreCase;

@Command(
        name = "add",
        description = "Добавляет пароль"
)
public class AddCommand implements Runnable {

    @CommandLine.ParentCommand
    private JanusCommand parent;

    @Parameters(index = "0", description = "Название ресурса")
    private char[] title;

    @Option(names = {"-s", "--site"}, description = "Сайт")
    private char[] site;

    @Option(names = {"-l", "--login"}, description = "Логин", interactive = true, required = true)
    private char[] login;

    @Option(names = {"-p", "--pass"}, description = "Пароль", interactive = true, required = true, arity = "0..1")
    private char[] password;

    @Option(names = {"-n", "--note"}, description = "Описание")
    private char[] note;

    @Option(
            names = {"-m", "--master"},
            description = "Мастер ключ",
            interactive = true,
            required = true
    )
    private char[] masterKey;

    @Override
    public void run() {
        List<PasswordEntry> entries = null;

        try {

            entries = StorageService.load(masterKey, parent.getVaultFilename());

            entries.add(new PasswordEntry(
                    title,
                    site,
                    login,
                    password,
                    note
            ));

            entries.sort(
                    (el1, el2) ->
                            compareToIgnoreCase(el1.getTitle(), el2.getTitle())
            );

            StorageService.save(entries, masterKey, parent.getVaultFilename());

            String message = "Запись успешно добавлена";
            System.out.println(
                    CommandLine.Help.Ansi.AUTO.string("@|green " + message + " |@"));
        } catch (Exception e) {
            String message = "Ошибка доступа. Проверьте мастер ключ";
            System.err.println(
                    CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@")
            );
        } finally {
            if (entries != null)
                entries.forEach(PasswordEntry::clean);
            if (masterKey != null) Arrays.fill(masterKey, '\0');
            if (title != null) Arrays.fill(title, '\0');
            if (site != null) Arrays.fill(site, '\0');
            if (login != null) Arrays.fill(login, '\0');
            if (password != null) Arrays.fill(password, '\0');
            if (note != null) Arrays.fill(note, '\0');

        }
    }


}
