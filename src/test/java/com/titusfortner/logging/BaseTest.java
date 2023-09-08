package com.titusfortner.logging;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class BaseTest {
    protected WebDriver driver;
    protected DriverService service;
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private static final PrintStream originalErr = System.err;
    protected Path logFile;

    @BeforeEach
    public void setup() {
        System.setErr(new PrintStream(err));
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
        if (service != null) {
            service.close();
        }
    }

    @AfterAll
    public static void restoreErr() {
        System.setErr(originalErr);
    }

    protected String getOutput() {
        return err.toString();
    }

    protected Boolean logFileContains(String text) {
        try (Stream<String> stream = Files.lines(logFile)) {
            return stream.anyMatch(line -> line.contains(text));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Long logFileSize() {
        if (logFile == null) {
            return null;
        }
        try (Stream<String> stream = Files.lines(logFile, StandardCharsets.UTF_8)) {
            return stream.count();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void createLogFile(String prefix) {
        try {
            logFile = Files.createTempFile(prefix + "-logging-", ".log");
            logFile.toFile().deleteOnExit();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
