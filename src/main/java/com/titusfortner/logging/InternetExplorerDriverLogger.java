package com.titusfortner.logging;

import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class InternetExplorerDriverLogger extends DriverLogger {
    public void setLevel(InternetExplorerDriverLogLevel level) {
        System.setProperty(getLogProperty(), level.name());
        ensureOutput();
    }

    public InternetExplorerDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : InternetExplorerDriverLogLevel.valueOf(logLevel);
    }

    public void all() {
        setLevel(InternetExplorerDriverLogLevel.TRACE);
    }

    protected void ensureLevel() {
        if (getLevel() == null) {
            setLevel(InternetExplorerDriverLogLevel.INFO);
        }
    }

    protected String getLogProperty() {
        return InternetExplorerDriverService.IE_DRIVER_LOGFILE_PROPERTY;
    }

    private String getLogLevelProperty() {
        return InternetExplorerDriverService.IE_DRIVER_LOGLEVEL_PROPERTY;
    }
}
