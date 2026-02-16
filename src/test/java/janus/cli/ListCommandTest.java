package janus.cli;

import org.janusvault.cli.JanusCommand;
import org.janusvault.model.PasswordEntry;
import org.janusvault.storage.StorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import picocli.CommandLine;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class ListCommandTest extends BaseCliTest {

    private List<PasswordEntry> getMockedEntries() {
        return Arrays.asList(
                new PasswordEntry("Google".toCharArray(), "google.com".toCharArray(), "googleUser".toCharArray(), "googlePass".toCharArray(), "githubNote".toCharArray()),
                new PasswordEntry("Github".toCharArray(), "github.com".toCharArray(), "githubUser".toCharArray(), "githubPass".toCharArray(), "githubNote".toCharArray()),
                new PasswordEntry("Vk".toCharArray(), "vk.com".toCharArray(), "vkUser".toCharArray(), "vkPass".toCharArray(), "vkNote".toCharArray())
        );
    }

//    @Test

    @Test
    void testShowMessageWhenVaultIsEmpty()
        throws Exception {

        provideInput("secret\n");

        try(MockedStatic<StorageService> mockedStorage =
                mockStatic(StorageService.class)){
            mockedStorage
                    .when(() -> StorageService.load(any(), any()))
                    .thenReturn(Collections.emptyList());

            JanusCommand janusCommand = new JanusCommand();
            CommandLine terminal = new CommandLine(janusCommand);
            terminal.execute("list", "-m");

            assertTrue(getOutput().contains("Хранилище пустое"));
        }
    }
}
