package org.janusvault;

import org.janusvault.cli.JanusCommand;

public class Main {
    public static void main(String[] args) {
        new picocli.CommandLine(new JanusCommand()).execute(args);
    }
}