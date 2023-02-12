package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleniumLoggerTest extends BaseTest {

    @Test
    public void defaultDoesNotContainExecutionOutput() {
        driver = new ChromeDriver();

        Assertions.assertTrue(getOutput().contains("ChromeDriver was started successfully"));
        Assertions.assertFalse(getOutput().contains("Executing: newSession"));
    }

    @Test
    public void warnDoesNotContainExecutionOutput() {
        seleniumLogger.setLevel(Level.WARNING);
        Assertions.assertEquals(seleniumLogger.getLevel(), Level.WARNING);

        driver = new ChromeDriver();

        Assertions.assertFalse(getOutput().contains("Executing: newSession"));
    }

    @Test
    public void fineDoesContainExecutionOutput() {
        seleniumLogger.setLevel(Level.FINE);
        Assertions.assertEquals(seleniumLogger.getLevel(), Level.FINE);

        driver = new ChromeDriver();

        Assertions.assertTrue(getOutput().contains("Executing: newSession"));
    }

    @Test
    public void defaultLoggedClasses() {
        List<String> classes = seleniumLogger.getLoggedClasses();
        Assertions.assertTrue(classes.contains(RemoteWebDriver.class.getName()));
        Assertions.assertTrue(classes.contains(SeleniumManager.class.getName()));
    }

    @Test
    public void addLoggedClass() {
        String className = ChromeDriver.class.getName();
        seleniumLogger.addLoggedClass(className);

        Assertions.assertTrue(seleniumLogger.getLoggedClasses().contains(className));
    }

    @Test
    public void removeLoggedClass() {
        seleniumLogger.setLevel(Level.FINE);
        String className = RemoteWebDriver.class.getName();
        seleniumLogger.removeLoggedClass(className);

        Assertions.assertFalse(seleniumLogger.getLoggedClasses().contains(className));

        driver = new ChromeDriver();
        Assertions.assertFalse(getOutput().contains("Executing: newSession"));
    }

    @Test
    public void defaultHandlerConsoleHandler() {
        Assertions.assertTrue(seleniumLogger.getHandler() instanceof ConsoleHandler);
    }


    @Test
    public void setHandler() {
        Handler handler = new ConsoleHandler();
        seleniumLogger.setHandler(handler);

        seleniumLogger.getLoggedClasses().forEach(logName -> {
            Arrays.stream(Logger.getLogger(logName).getHandlers()).forEach(h -> {
                Assertions.assertEquals(h, handler);
            });
        });
    }

    @Test
    public void defaultLevelInfo() {
        Assertions.assertEquals(seleniumLogger.getHandler().getLevel(), Level.INFO);

        seleniumLogger.getLoggedClasses().forEach(logName -> {
            Arrays.stream(Logger.getLogger(logName).getHandlers()).forEach(h -> {
                Assertions.assertEquals(h.getLevel(), Level.INFO);
            });
        });
    }

    @Test
    public void setLevelChangesHandlersLevel() {
        seleniumLogger.setLevel(Level.FINE);

        Assertions.assertEquals(seleniumLogger.getHandler().getLevel(), Level.FINE);

        seleniumLogger.getLoggedClasses().forEach(logName -> {
            Arrays.stream(Logger.getLogger(logName).getHandlers()).forEach(h -> {
                Assertions.assertEquals(h.getLevel(), Level.FINE);
            });
        });
    }

    @Test
    public void defaultFileOutput() {
        Assertions.assertNull(seleniumLogger.getFileOutput());
    }

    @Test
    public void setFileOutputDoesNotLogToConsole() {
        try {
            seleniumLogger.setLevel(Level.FINE);
            Path path = Files.createTempFile("selenium-logging-", ".log");
            File file = path.toFile();
            seleniumLogger.setFileOutput(file);

            driver = new ChromeDriver();

            String expectedText = "Executing: newSession";
            Assertions.assertFalse(getOutput().contains(expectedText));

            Assertions.assertTrue(Files.readAllLines(path).toString().contains(expectedText));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
