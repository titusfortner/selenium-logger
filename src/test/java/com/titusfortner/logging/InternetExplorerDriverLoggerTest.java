package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.service.DriverService;

import java.time.Year;

@EnabledOnOs(OS.WINDOWS)
public class InternetExplorerDriverLoggerTest extends BaseTest {
    private InternetExplorerDriverLogger internetExplorerDriverLogger;

    @BeforeEach
    public void setIeDriverLogger() {
        internetExplorerDriverLogger = new InternetExplorerDriverLogger();
        System.clearProperty(InternetExplorerDriverService.IE_DRIVER_LOGFILE_PROPERTY);
        System.clearProperty(InternetExplorerDriverService.IE_DRIVER_LOGLEVEL_PROPERTY);
    }

    @Test
    public void defaultLogsNothing() {
        logsInfo();

        Assertions.assertNull(internetExplorerDriverLogger.getLevel());
        Assertions.assertNull(internetExplorerDriverLogger.getOutput());
        Assertions.assertTrue(getOutput().isEmpty());
    }

    @Test
    public void enableLogsInfo() {
        InternetExplorerDriverLogger.enable();

        logsInfo();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.INFO, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, internetExplorerDriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("I " + Year.now()));
    }

    @Test
    public void disableLogs() {
        InternetExplorerDriverLogger.enable();
        internetExplorerDriverLogger.disable();

        logsInfo();

        Assertions.assertEquals(DriverService.LOG_NULL, internetExplorerDriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
    }

    @Test
    public void disableIgnoresLevel() {
        internetExplorerDriverLogger.setLevel(InternetExplorerDriverLogLevel.DEBUG);
        internetExplorerDriverLogger.disable();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.DEBUG, internetExplorerDriverLogger.getLevel());
    }

    @Test
    public void setLogLevel() {
        internetExplorerDriverLogger.setLevel(InternetExplorerDriverLogLevel.DEBUG);

        logsInfo();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.DEBUG, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, internetExplorerDriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("D " + Year.now()));
    }

    @Test
    public void setLevelIgnoresOutput() {
        createLogFile("ieDriver");
        internetExplorerDriverLogger.setFile(logFile.toFile());

        internetExplorerDriverLogger.setLevel(InternetExplorerDriverLogLevel.DEBUG);

        Assertions.assertEquals(InternetExplorerDriverLogLevel.DEBUG, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), internetExplorerDriverLogger.getOutput());
    }

    @Test
    public void setLogFile() {
        createLogFile("ieDriver");
        internetExplorerDriverLogger.setFile(logFile.toFile());

        logsInfo();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.INFO, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), internetExplorerDriverLogger.getOutput());
        Assertions.assertEquals("", getOutput());
        Assertions.assertTrue(logFileContains("I " + Year.now()));
    }

    @Test
    public void setFileIgnoresLevel() {
        internetExplorerDriverLogger.setLevel(InternetExplorerDriverLogLevel.DEBUG);
        createLogFile("ieDriver");
        internetExplorerDriverLogger.setFile(logFile.toFile());

        Assertions.assertEquals(InternetExplorerDriverLogLevel.DEBUG, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), internetExplorerDriverLogger.getOutput());
        Assertions.assertEquals(logFile.toFile(), internetExplorerDriverLogger.getFile());
    }

    @Test
    public void allLogLevels() {
        internetExplorerDriverLogger.all();

        logsInfo();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.TRACE, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(DriverService.LOG_STDERR, internetExplorerDriverLogger.getOutput());
        Assertions.assertTrue(getOutput().contains("T " + Year.now()));
    }

    @Test
    public void allLogsIgnoresOutput() {
        createLogFile("ieDriver");
        internetExplorerDriverLogger.setFile(logFile.toFile());
        internetExplorerDriverLogger.all();

        logsInfo();

        Assertions.assertEquals(InternetExplorerDriverLogLevel.TRACE, internetExplorerDriverLogger.getLevel());
        Assertions.assertEquals(logFile.toString(), internetExplorerDriverLogger.getOutput());
        Assertions.assertFalse(getOutput().contains("T " + Year.now()));
    }

    private void logsInfo() {
        driver = new InternetExplorerDriver();
    }
}
