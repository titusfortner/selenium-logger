package com.titusfortner.logging;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public class ChromeDriverLogger {

    public void enable() {
        ensureLevel();
        ensureOutput();
    }

    public void all() {
        setLevel(ChromiumDriverLogLevel.ALL);
    }

    public void disable() {
        System.setProperty(getLogProperty(), DriverService.LOG_NULL);
    }

    public void setLevel(ChromiumDriverLogLevel level) {
        System.setProperty(getLogLevelProperty(), level.name());
        ensureOutput();
    }

    public ChromiumDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : ChromiumDriverLogLevel.valueOf(logLevel);
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

    public void appendToLog(File logFile) {
        setFile(logFile);
        System.setProperty(getAppendLogProperty(), String.valueOf(true));
    }

    public void setReadableTimestamp(File logFile) {
        setFile(logFile);
        System.setProperty(getReadableTimestampProperty(), String.valueOf(true));
    }

    private void ensureLevel() {
        if (getLevel() == null) {
            setLevel(ChromiumDriverLogLevel.INFO);
        }
    }

    private void ensureOutput() {
        if (getOutput() == null) {
            System.setProperty(getLogProperty(), DriverService.LOG_STDERR);
        }
    }

    private String getLogProperty() {
        return ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY;
    }
    private String getReadableTimestampProperty() {
        return ChromeDriverService.CHROME_DRIVER_READABLE_TIMESTAMP;
    }
    private String getLogLevelProperty() {
        return ChromeDriverService.CHROME_DRIVER_LOG_LEVEL_PROPERTY;
    }
    private String getAppendLogProperty() {
        return ChromeDriverService.CHROME_DRIVER_APPEND_LOG_PROPERTY;
    }
}
