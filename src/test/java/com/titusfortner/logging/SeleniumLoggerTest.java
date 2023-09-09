package com.titusfortner.logging;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverFinder;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.SimpleFormatter;

public class SeleniumLoggerTest extends BaseTest {
    private SeleniumLogger seleniumLogger;

    @BeforeEach
    public void initialize() {
        seleniumLogger = new SeleniumLogger();
    }

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
    public void turnsOnAllLogs() {
        seleniumLogger = SeleniumLogger.all();

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINEST [HttpURLConnection"));
        Assertions.assertTrue(getOutput().contains("FINEST [ClientVector"));
    }

    @Test
    public void disableLogs() {
        seleniumLogger.disable();

        logsWarning();

        Assertions.assertEquals("", getOutput());
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
    public void defaultFilteredClasses() {
        seleniumLogger.setLevel(Level.FINE);
        seleniumLogger.setFilter(new SeleniumFilter());

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [RemoteWebDriver"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [DriverService"));
    }

    @Test
    public void addToBlockedList() {
        seleniumLogger.setLevel(Level.FINE);
        SeleniumFilter filter = new SeleniumFilter();
        filter.addBlocked("SeleniumManager");
        seleniumLogger.setFilter(filter);

        logsMultipleClasses();

        Assertions.assertFalse(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [RemoteWebDriver"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [DriverService"));
    }

    @Test
    public void addToAllowedList() {
        seleniumLogger.setLevel(Level.FINE);
        SeleniumFilter filter = new SeleniumFilter();
        filter.addAllowed("http");
        seleniumLogger.setFilter(filter);

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINE [HttpClientImpl"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [DriverService"));
    }

    @Test
    public void addFilter() {
        seleniumLogger.setLevel(Level.FINE);
        seleniumLogger.filter("SeleniumManager");

        logsMultipleClasses();

        Assertions.assertFalse(getOutput().contains("FINE [HttpClientImpl"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertFalse(getOutput().contains("FINE Selenium [DriverService"));
    }

    @Test
    public void mixAllowAndBlockLists() {
        seleniumLogger.setLevel(Level.FINE);
        SeleniumFilter filter = new SeleniumFilter();
        filter.addBlocked("SeleniumManager");
        filter.addAllowed("http");
        seleniumLogger.setFilter(filter);

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINE [HttpClientImpl"));
        Assertions.assertFalse(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [DriverService"));
    }

    @Test
    public void defaultLoggedClasses() {
        seleniumLogger.setLevel(Level.FINE);

        logsMultipleClasses();

        Assertions.assertTrue(getOutput().contains("FINE Selenium [SeleniumManager"));
        Assertions.assertTrue(getOutput().contains("FINE Selenium [RemoteWebDriver"));
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
        seleniumLogger.filter("OpenTelemetryTracer");
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        driver = new ChromeDriver(options);
    }
}
