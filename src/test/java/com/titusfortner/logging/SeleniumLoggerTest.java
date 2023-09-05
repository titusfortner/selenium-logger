package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverFinder;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.*;

public class SeleniumLoggerTest extends BaseTest {

    @Test
    public void defaultLogsINFO() {
        logsInfo();

        String out = "INFO Selenium [OpenTelemetry";
        Assertions.assertTrue(getOutput().contains(out));
    }

    @Test
    public void enableLogsFINE() {
        seleniumLogger = SeleniumLogger.enable();
        Assertions.assertEquals(seleniumLogger.getLevel(), Level.FINE);

        logsFine();

        String out = "FINE Selenium [SeleniumManager";
        Assertions.assertTrue(getOutput().contains(out));
    }

    @Test
    public void setLevelInfoGetsWarningNotFine() {
        logsFine();
        logsWarning();

        String warning = "WARNING Selenium [SeleniumManager";
        Assertions.assertTrue(getOutput().contains(warning));
        String fine = "FINE Selenium [SeleniumManager";
        Assertions.assertFalse(getOutput().contains(fine));
    }

    @Test
    public void setFormatter() {
        seleniumLogger.setFormatter(new SimpleFormatter());
        logsWarning();

        String fine = "WARNING: chrome banana not available for download";
        Assertions.assertTrue(getOutput().contains(fine));
    }

    @Test
    public void getFormatter() {
        Assertions.assertSame(seleniumLogger.getFormatter().getClass(), SeleniumFormatter.class);
    }


    @Test
    public void defaultLoggedClasses() {
        seleniumLogger.setLevel(Level.FINE);

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [RemoteWebDriver"));
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
        String className = SeleniumManager.class.getName();
        seleniumLogger.removeLoggedClass(className);

        Assertions.assertFalse(seleniumLogger.getLoggedClasses().contains(className));

        logsFine();
        Assertions.assertEquals("", getOutput());
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
        createLogFile("selenium");
        seleniumLogger.setFileOutput(logFile.toFile());

        logsWarning();

        Assertions.assertEquals("", getOutput());
        String out = "WARNING Selenium [SeleniumManager";
        Assertions.assertTrue(logFileContains(out));
    }

    // This is the only place Selenium uses INFO
    // It's a singleton, so it only works once
    private void logsInfo() {
        seleniumLogger.addLoggedClass("org.openqa.selenium.remote.tracing.opentelemetry.OpenTelemetryTracer");
        try {
            URL url = new URL("http://localhost");
            ChromeOptions options = new ChromeOptions();

            new RemoteWebDriver(url, options);
        } catch (Throwable e) {
            // ignore
        }
    }

    // Code that Generates a Selenium Manager WARNING in logs
    private void logsWarning() {
        try {
            ChromeDriverService service = ChromeDriverService.createDefaultService();
            ChromeOptions options = new ChromeOptions();
            options.setBrowserVersion("banana");
            DriverFinder.getPath(service, options, true);
        } catch (Throwable e) {
            // ignore
        }
    }

    // Code that Generates a Selenium Manager FINE in logs
    private void logsFine() {
        try {
            DriverFinder.getPath(ChromeDriverService.createDefaultService(), new ChromeOptions(), true);
        } catch (Throwable e) {
            // ignore
        }
    }

    private void logsMultipleClasses() {
        driver = new ChromeDriver();
    }
}
