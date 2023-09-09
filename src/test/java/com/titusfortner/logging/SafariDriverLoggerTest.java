package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariDriverService;

@EnabledOnOs(OS.MAC)
public class SafariDriverLoggerTest extends BaseTest {
    private SafariDriverLogger safariDriverLogger;

    @BeforeEach
    public void enableLogging() {
        safariDriverLogger = new SafariDriverLogger();
        System.clearProperty(SafariDriverService.SAFARI_DRIVER_LOGGING);
    }

    @Test
    public void defaultIsNoLogging() {
        int before = safariDriverLogger.getList().size();

        driver = new SafariDriver();

        Assertions.assertEquals(before, safariDriverLogger.getList().size());
    }

    @Test
    public void enableLogs() {
        safariDriverLogger.enable();

        int before = safariDriverLogger.getList().size();
        driver = new SafariDriver();

        Assertions.assertEquals(before + 1, safariDriverLogger.getList().size());
    }

    @Test
    public void getFile() {
        safariDriverLogger.enable();

        driver = new SafariDriver();

        logFile = safariDriverLogger.getFile().toPath();
        Assertions.assertTrue(logFileContains("AutomationProtocol: SEND"));
    }
}
