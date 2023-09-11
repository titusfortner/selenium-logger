package com.titusfortner.logging;

import org.openqa.selenium.chromium.ChromiumDriverLogLevel;

import java.io.File;

public abstract class ChromiumDriverLogger extends DriverLogger {
    protected abstract String getLogLevelProperty();
    protected abstract String getAppendLogProperty();
    protected abstract String getReadableTimestampProperty();

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
}
