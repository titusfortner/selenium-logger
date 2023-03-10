package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
public class GeckoDriverLoggerTest extends BaseTest {
    @Test
    public void defaultDisablesGeckoDriverOutput() {
        seleniumLogger.setLevel(Level.WARNING);

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void enableGeckoDriverOutput() {
        seleniumLogger.setLevel(Level.WARNING);
        seleniumLogger.geckodriver().enable();

        driver = new FirefoxDriver();

        Assertions.assertTrue(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void disableGeckoDriverOutput() {
        seleniumLogger.setLevel(Level.WARNING);
        seleniumLogger.geckodriver().enable();
        seleniumLogger.geckodriver().disable();

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void ignoreGeckoDriverOutputWhenAlreadySet() throws IOException {
        String logfileProperty = FirefoxDriver.SystemProperty.BROWSER_LOGFILE;
        Path path = Files.createTempFile("geckodriver-logging-", ".log");

        System.setProperty(logfileProperty, path.toString());

        seleniumLogger.setLevel(Level.WARNING);

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
        Assertions.assertTrue(Files.readAllLines(path).toString().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void setsGeckoDriverLogPath() throws IOException {
        Path path = Files.createTempFile("geckodriver-logging-", ".log");

        seleniumLogger.setLevel(Level.WARNING);
        seleniumLogger.geckodriver().setFile(path.toFile());

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
        Assertions.assertTrue(Files.readAllLines(path).toString().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void getsGeckoDriverLogPath() throws IOException {
        String logfileProperty = FirefoxDriver.SystemProperty.BROWSER_LOGFILE;
        Path path = Files.createTempFile("geckodriver-logging-", ".log");
        System.setProperty(logfileProperty, path.toString());

        Assertions.assertEquals(path.toFile(), seleniumLogger.geckodriver().getFile());
    }
}
