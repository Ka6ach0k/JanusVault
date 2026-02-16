package janus.cli;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

public abstract class BaseCliTest {

    private final ByteArrayOutputStream outCustom = new ByteArrayOutputStream();
    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outCustom));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
    }

    protected void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    protected String getOutput() {
        return outCustom.toString();
    }
}
