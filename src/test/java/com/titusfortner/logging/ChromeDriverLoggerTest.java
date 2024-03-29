package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.remote.service.DriverService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChromeDriverLoggerTest extends BaseTest {
    private ChromeDriverLogger chromedriverLogger;
    @BeforeEach
    public void setChromedriverLogger() {
        chromedriverLogger = new ChromeDriverLogger();
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY);
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_READABLE_TIMESTAMP);
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_LOG_LEVEL_PROPERTY);
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_APPEND_LOG_PROPERTY);
        System.clearProperty(ChromeDriverService.CHROME_DRIVER_VERBOSE_LOG_PROPERTY);
    }

    @Test
    public void defaultLogsNothing() {
        logsInfo();

        Assertions.assertNull(chromedriverLogger.getLevel());
        Assertions.assertNull(chromedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
    }

    @Test
    public void enableLogsInfo() {
        ChromeDriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, chromedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, chromedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[INFO]"));
    }

    @Test
    public void disableLogs() {
        ChromeDriverLogger.enable();
        chromedriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, chromedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_NULL, chromedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().isEmpty());
    }

    @Test
    public void disableIgnoresLevel() {
        chromedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);
        chromedriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, chromedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_NULL, chromedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().isEmpty());
    }

    @Test
    public void setLogLevel() {
        chromedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, chromedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, chromedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void setLevelIgnoresOutput() {
        createLogFile("chromedriver");
        chromedriverLogger.setFile(logFile.toFile());
        chromedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, chromedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), chromedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("[DEBUG]"));
    }

    @Test
    public void setLogFile() {
        createLogFile("chromedriver");
        chromedriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, chromedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), chromedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("[INFO]"));
    }

    @Test
    public void setFileIgnoresLevel() {
        chromedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);
        createLogFile("chromedriver");
        chromedriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, chromedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), chromedriverLogger.getOutput());
        Assertions.assertTrue(logFileContains("[DEBUG]"));
    }

    @Test
    public void allLogLevels() {
        chromedriverLogger.all();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.ALL, chromedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, chromedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void allLogsIgnoresOutput() {
        createLogFile("chromedriver");
        chromedriverLogger.setFile(logFile.toFile());
        chromedriverLogger.all();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.ALL, chromedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), chromedriverLogger.getOutput());
        Assertions.assertFalse(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void withoutAppend()  {
        createLogFile("chromedriver");
        chromedriverLogger.setFile(logFile.toFile());

        logsStartStop();

        long initialSize = logFileSize();

        logsStartStop();

        Assertions.assertEquals(logFileSize(), initialSize);
    }

    @Test
    public void appendLogs() {
        createLogFile("chromedriver-append");
        chromedriverLogger.appendToLog(logFile.toFile());

        logsStartStop();

        long initialSize = logFileSize();

        logsStartStop();

        Assertions.assertEquals(initialSize*2, logFileSize());
    }

    @Test
    public void setReadableTimestamp() {
        createLogFile("chromedriver");
        chromedriverLogger.setReadableTimestamp(logFile.toFile());

        logsInfo();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-uuuu ");
        LocalDate localDate = LocalDate.now();
        Assertions.assertTrue(logFileContains("[" + dtf.format(localDate)));
    }

    private void logsInfo() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
    }

    private void logsStartStop() {
        logsInfo();
        driver.quit();
    }
}
