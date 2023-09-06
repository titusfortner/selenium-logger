package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;

public class GeckoDriverLoggerTest extends BaseTest {
    private GeckoDriverLogger geckodriverLogger;
    @BeforeEach
    public void setFirefoxDriverLogger() {
        geckodriverLogger = new GeckoDriverLogger();
        System.clearProperty(GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY);
        System.clearProperty(GeckoDriverService.GECKO_DRIVER_LOG_LEVEL_PROPERTY);
    }

    @Test
    public void defaultLogsNothing() {
        logsInfo();

        Assertions.assertNull(geckodriverLogger.getLevel());
        Assertions.assertNull(geckodriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
    }

    @Test
    public void enableLogsInfo() {
        geckodriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(FirefoxDriverLogLevel.INFO, geckodriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, geckodriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("\tINFO\t"));
    }

    @Test
    public void enableIgnoresLevel() {
        geckodriverLogger.setLevel(FirefoxDriverLogLevel.DEBUG);
        geckodriverLogger.enable();

        Assertions.assertEquals(geckodriverLogger.getLevel(), FirefoxDriverLogLevel.DEBUG);
        Assertions.assertEquals(DriverService.LOG_STDERR, geckodriverLogger.getOutput());
    }

    @Test
    public void enableIgnoresOutput() {
        createLogFile("firefoxDriver");
        geckodriverLogger.setFile(logFile.toFile());
        geckodriverLogger.enable();

        driver = new FirefoxDriver();

        Assertions.assertEquals(FirefoxDriverLogLevel.INFO, geckodriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), geckodriverLogger.getOutput());
    }

    @Test
    public void disableGeckoDriverOutput() {
        geckodriverLogger.enable();
        geckodriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(DriverService.LOG_NULL, geckodriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
    }

    @Test
    public void disableIgnoresLevel() {
        geckodriverLogger.setLevel(FirefoxDriverLogLevel.DEBUG);
        geckodriverLogger.disable();

        Assertions.assertEquals(FirefoxDriverLogLevel.DEBUG, geckodriverLogger.getLevel());
    }

    @Test
    public void setLevelEnables() {
        geckodriverLogger.setLevel(FirefoxDriverLogLevel.DEBUG);

        logsDebug();

        Assertions.assertEquals(FirefoxDriverLogLevel.DEBUG, geckodriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, geckodriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("\tDEBUG\t"));
    }

    @Test
    public void setLevelIgnoresOutput() {
        createLogFile("firefoxDriver");
        geckodriverLogger.setFile(logFile.toFile());

        geckodriverLogger.setLevel(FirefoxDriverLogLevel.DEBUG);

        Assertions.assertEquals(FirefoxDriverLogLevel.DEBUG, geckodriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), geckodriverLogger.getOutput());
    }

    @Test
    public void setsFile() {
        createLogFile("firefoxDriver");
        geckodriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(FirefoxDriverLogLevel.INFO, geckodriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), geckodriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("\tINFO\t"));
    }

    @Test
    public void setFileIgnoresLevel() {
        geckodriverLogger.setLevel(FirefoxDriverLogLevel.DEBUG);
        createLogFile("firefoxDriver");
        geckodriverLogger.setFile(logFile.toFile());

        Assertions.assertEquals(FirefoxDriverLogLevel.DEBUG, geckodriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), geckodriverLogger.getOutput());
        Assertions.assertEquals(logFile.toFile(), geckodriverLogger.getFile());
    }

    private void logsInfo() {
        service = new GeckoDriverService.Builder().build();
        try {
            service.start();
        } catch (IOException e) {
            // ignore
        }
    }

    private void logsDebug() {
        driver = new FirefoxDriver();
    }
}
