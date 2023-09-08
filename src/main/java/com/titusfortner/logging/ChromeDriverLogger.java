package com.titusfortner.logging;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;

import java.io.File;

public class ChromeDriverLogger extends DriverLogger{
    public void all() {
        setLevel(ChromiumDriverLogLevel.ALL);
    }

    public void setLevel(ChromiumDriverLogLevel level) {
        System.setProperty(getLogLevelProperty(), level.name());
        ensureOutput();
    }

    public ChromiumDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : ChromiumDriverLogLevel.valueOf(logLevel);
    }

    public void appendToLog(File logFile) {
        setFile(logFile);
        System.setProperty(getAppendLogProperty(), String.valueOf(true));
    }

    public void setReadableTimestamp(File logFile) {
        setFile(logFile);
        System.setProperty(getReadableTimestampProperty(), String.valueOf(true));
    }

    protected void ensureLevel() {
        if (getLevel() == null) {
            setLevel(ChromiumDriverLogLevel.INFO);
        }
    }

    protected String getLogProperty() {
        return ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY;
    }
    protected String getReadableTimestampProperty() {
        return ChromeDriverService.CHROME_DRIVER_READABLE_TIMESTAMP;
    }
    protected String getLogLevelProperty() {
        return ChromeDriverService.CHROME_DRIVER_LOG_LEVEL_PROPERTY;
    }
    protected String getAppendLogProperty() {
        return ChromeDriverService.CHROME_DRIVER_APPEND_LOG_PROPERTY;
    }
}
