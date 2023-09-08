package com.titusfortner.logging;

import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public class GeckoDriverLogger {

    public void enable() {
        ensureLevel();
        ensureOutput();
    }

    public void all() {
        setLevel(FirefoxDriverLogLevel.TRACE);
    }

    public void disable() {
        System.setProperty(getLogProperty(), DriverService.LOG_NULL);
    }

    public void setLevel(FirefoxDriverLogLevel level) {
        System.setProperty(getLogLevelProperty(), level.name());
        ensureOutput();
    }

    public FirefoxDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : FirefoxDriverLogLevel.valueOf(logLevel);
    }

    public String getOutput() {
        return System.getProperty(getLogProperty());
    }

    public void setFile(File fileName) {
        System.setProperty(getLogProperty(), fileName.getAbsolutePath());
        ensureLevel();
    }

    public File getFile() {
        String path = getOutput();
        return path == null || !new File(path).isFile() ? null : new File(path);
    }

    public void setTruncate(Boolean truncate) {
        System.setProperty(getTruncateProperty(), String.valueOf(!truncate));
        ensureLevel();
        ensureOutput();
    }

    private void ensureLevel() {
        if (getLevel() == null) {
            setLevel(FirefoxDriverLogLevel.INFO);
        }
    }

    private void ensureOutput() {
        if (getOutput() == null) {
            System.setProperty(getLogProperty(), DriverService.LOG_STDERR);
        }
    }

    private String getLogProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY;
    }

    private String getLogLevelProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_LEVEL_PROPERTY;
    }

    private String getTruncateProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_NO_TRUNCATE;
    }
}
