package org.janusvault.cli;

import org.janusvault.storage.ConfigService;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ScopeType;

@Command(name = "Janus",
        mixinStandardHelpOptions = true,
        description = "JanusVault — Двуликий защитник ваших секретов",
        subcommands = {
                AddCommand.class,
                ListCommand.class,
                InitCommand.class,
                SetDefaultCommand.class
        }//, DeleteCommand.class, UpdateCommand.class}
)
public class JanusCommand implements Runnable {

    @Option(
            names = {"-v", "--vault", "-f", "--file"},
            description = "Имя хранилища или полный путь к файлу",
            scope = ScopeType.INHERIT
    )
    protected String vaultFilename;

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }

    public String getVaultFilename() {
        return ConfigService.resolvePath(vaultFilename);
    }
}
