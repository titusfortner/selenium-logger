package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EdgeDriverLoggerTest extends BaseTest {
    private EdgeDriverLogger edgedriverLogger;
    @BeforeEach
    public void setEdgedriverLogger() {
        edgedriverLogger = new EdgeDriverLogger();
        System.clearProperty(EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY);
        System.clearProperty(EdgeDriverService.EDGE_DRIVER_READABLE_TIMESTAMP);
        System.clearProperty(EdgeDriverService.EDGE_DRIVER_LOG_LEVEL_PROPERTY);
        System.clearProperty(EdgeDriverService.EDGE_DRIVER_APPEND_LOG_PROPERTY);
        System.clearProperty(EdgeDriverService.EDGE_DRIVER_VERBOSE_LOG_PROPERTY);
    }

    @Test
    public void defaultLogsNothing() {
        logsInfo();

        Assertions.assertNull(edgedriverLogger.getLevel());
        Assertions.assertNull(edgedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
    }

    @Test
    public void enableLogsInfo() {
        edgedriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[INFO]"));
    }

    @Test
    public void enableDoesNotOverwriteLevel() {
        edgedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);
        edgedriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void enableDoesNotOverwriteOutput() {
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());
        edgedriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, edgedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), edgedriverLogger.getOutput());
        Assertions.assertFalse(getOutput().contains("[INFO]"));
        Assertions.assertTrue(logFileContains("[INFO]"));
    }

    @Test
    public void disableLogs() {
        edgedriverLogger.enable();
        edgedriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_NULL, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().isEmpty());
    }

    @Test
    public void disableIgnoresLevel() {
        edgedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);
        edgedriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_NULL, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().isEmpty());
    }

    @Test
    public void setLogLevel() {
        edgedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void setLevelIgnoresOutput() {
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());
        edgedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, edgedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), edgedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("[DEBUG]"));
    }

    @Test
    public void setLogFile() {
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.INFO, edgedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), edgedriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("[INFO]"));
    }

    @Test
    public void setFileIgnoresLevel() {
        edgedriverLogger.setLevel(ChromiumDriverLogLevel.DEBUG);
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.DEBUG, edgedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), edgedriverLogger.getOutput());
        Assertions.assertTrue(logFileContains("[DEBUG]"));
    }

    @Test
    public void allLogLevels() {
        edgedriverLogger.all();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.ALL, edgedriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, edgedriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void allLogsIgnoresOutput() {
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());
        edgedriverLogger.all();

        logsInfo();

        Assertions.assertEquals(ChromiumDriverLogLevel.ALL, edgedriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), edgedriverLogger.getOutput());
        Assertions.assertFalse(getOutput().contains("[DEBUG]"));
    }

    @Test
    public void withoutAppend()  {
        createLogFile("edgedriver");
        edgedriverLogger.setFile(logFile.toFile());

        driver = new EdgeDriver();
        driver.quit();

        long initialSize = logFileSize();

        driver = new EdgeDriver();
        driver.quit();

        Assertions.assertEquals(logFileSize(), initialSize);
    }

    @Test
    public void appendLogs() throws IOException {
        createLogFile("edgedriver-append");
        edgedriverLogger.appendToLog(logFile.toFile());

        driver = new EdgeDriver();
        driver.quit();

        long initialSize = logFileSize();

        driver = new EdgeDriver();
        driver.quit();

        Assertions.assertEquals(initialSize*2, logFileSize());
    }

    @Test
    public void setReadableTimestamp() {
        createLogFile("edgedriver");
        edgedriverLogger.setReadableTimestamp(logFile.toFile());

        logsInfo();

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd-uuuu ");
        LocalDate localDate = LocalDate.now();
        Assertions.assertTrue(logFileContains("[" + dtf.format(localDate)));
    }

    private void logsInfo() {
        driver = new EdgeDriver();
    }
}
