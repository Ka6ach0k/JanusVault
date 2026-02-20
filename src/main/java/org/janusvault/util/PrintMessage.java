package org.janusvault.util;

import picocli.CommandLine;

public class PrintMessage {
    public static void printError(String message) {
        System.err.println(CommandLine.Help.Ansi.AUTO.string("@|red " + message + " |@"));
    }

    public static void printWarning(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|yellow " + message + " |@"));
    }

    public static void printSuccess(String message) {
        System.out.println(CommandLine.Help.Ansi.AUTO.string("@|green " + message + " |@"));
    }

}
