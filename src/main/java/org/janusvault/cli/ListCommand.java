package org.janusvault.cli;

import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.janusvault.util.CharUtil.containsIgnoreCase;

@Command(
        name = "list",
        mixinStandardHelpOptions = true,
        description = "Показывает список паролей"
)
public class ListCommand implements Runnable {

    @CommandLine.ParentCommand
    private JanusCommand parent;

    @Parameters(
            index = "0",
            arity = "0..1",
            description = "Паттерн для поиска"
    )
    private String pattern;

    @Option(
            names = {"-a", "--available"},
            description = "Показать список всех доступных хранилищ"
    )
    private boolean showAvailable;

    @Option(
            names = {"-m", "--master"},
            description = "Мастер ключ",
            interactive = true
    )
    private char[] masterKey;

    @Override
    public void run() {

        if (showAvailableVault())
            return;

        if (masterKey == null) {
            printErrorMessage("Не указан мастер ключ");
            return;
        }

        showVault();
    }

    private boolean showAvailableVault() {
        if (showAvailable) {
            List<String> vaults = StorageService.getAvailableVaults();

            if (vaults.isEmpty())
                printWarningMessage("Хранилищ пока нет");
            else {
                String message = "Доступные хранилища";
                System.out.println(
                        CommandLine.Help.Ansi.AUTO.string("@|bold,blue " + message + " |@")
                );
                vaults.forEach(vault -> System.out.println("- " + vault));
            }
            return true;
        }
        return false;
    }

    private void showVault() {
        List<PasswordEntry> entries = null;
        try {
            entries = StorageService.load(masterKey, parent.getVaultFilename());

            if (entries.isEmpty()) {
                printWarningMessage("Хранилище пустое");
                return;
            }

            List<PasswordEntry> filteredEntries = entries.stream()
                    .filter(this::matcherPattern)
                    .collect(Collectors.toList());

            if (filteredEntries.isEmpty()) {
                printWarningMessage("Ничего не найдено");
                return;
            }

            printVertical(filteredEntries);

        } catch (Exception e) {
            printErrorMessage("Ошибка доступа. Проверьте мастер ключ");
        } finally {
            clean(entries);
        }
    }

    private void clean(List<PasswordEntry> entries) {
        if (entries != null)
            entries.forEach(PasswordEntry::clean);
        if (masterKey != null)
            Arrays.fill(masterKey, '\0');
    }

    private void printVertical(List<PasswordEntry> entries) {
        CommandLine.Help.Ansi ansi = CommandLine.Help.Ansi.AUTO;

        for (PasswordEntry entry : entries) {
            System.out.println(ansi.string("@|bold,blue -------------------- |@"));
            printRow("TITLE", entry.getTitle());
            printRow("SITE", entry.getSite());
            printRow("LOGIN", entry.getLogin());
            printRow("PASSWORD", entry.getPassword());
            printRow("NOTE", entry.getNote());
        }
        System.out.println(ansi.string("@|bold,blue -------------------- |@"));
    }

    private void printRow(String label, char[] data) {
        if (data == null)
            return;

        String formatLabel = String.format("@|bold %-10s|@", label + ":");
        System.out.print(CommandLine.Help.Ansi.AUTO.string(formatLabel));

        for (char c : data)
            System.out.print(c);
        System.out.println();
    }


    private boolean matcherPattern(PasswordEntry entry) {
        if (pattern == null || pattern.isBlank())
            return true;

        return containsIgnoreCase(entry.getTitle(), pattern.toCharArray()) ||
                containsIgnoreCase(entry.getSite(), pattern.toCharArray());
    }

    private void printErrorMessage(String message) {
        System.err.println(
                CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@"));
    }

    private void printWarningMessage(String message) {
        System.out.println(
                CommandLine.Help.Ansi.AUTO.string("@|yellow " + message + " |@"));
    }

}
