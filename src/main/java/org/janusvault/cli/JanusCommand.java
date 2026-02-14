package org.janusvault.cli;

import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.nio.file.Paths;

@Command(name = "Janus",
        mixinStandardHelpOptions = true,
        description = "JanusVault — двуликий защитник ваших секретов",
        subcommands = {AddCommand.class, ListCommand.class, DeleteCommand.class, UpdateCommand.class})
public class JanusCommand implements Runnable {
    public static final String DEFAULT_PATH =
            Paths.get(System.getProperty("user.home"), ".janusvault").toString();

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
