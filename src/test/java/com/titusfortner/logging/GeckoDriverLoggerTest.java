package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.GeckoDriverService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GeckoDriverLoggerTest extends BaseTest {
    private final GeckoDriverLogger geckodriverLogger = new GeckoDriverLogger();

    @Test
    public void defaultDisablesGeckoDriverOutput() {
        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void enableGeckoDriverOutput() {
        geckodriverLogger.enable();

        driver = new FirefoxDriver();

        Assertions.assertTrue(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void disableGeckoDriverOutput() {
        geckodriverLogger.enable();
        geckodriverLogger.disable();

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void ignoreGeckoDriverOutputWhenAlreadySet() throws IOException {
        String logfileProperty = GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY;
        Path path = Files.createTempFile("geckodriver-logging-", ".log");

        System.setProperty(logfileProperty, path.toString());

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
        Assertions.assertTrue(Files.readAllLines(path).toString().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void setsGeckoDriverLogPath() throws IOException {
        Path path = Files.createTempFile("geckodriver-logging-", ".log");
        geckodriverLogger.setFile(path.toFile());

        driver = new FirefoxDriver();

        Assertions.assertFalse(getOutput().contains("geckodriver\tINFO\tListening on"));
        Assertions.assertTrue(Files.readAllLines(path).toString().contains("geckodriver\tINFO\tListening on"));
    }

    @Test
    public void getsGeckoDriverLogPath() throws IOException {
        String logfileProperty = GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY;
        Path path = Files.createTempFile("geckodriver-logging-", ".log");
        System.setProperty(logfileProperty, path.toString());

        Assertions.assertEquals(path.toFile(), geckodriverLogger.getFile());
    }
}
