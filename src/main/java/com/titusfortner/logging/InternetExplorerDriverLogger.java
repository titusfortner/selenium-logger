package com.titusfortner.logging;

import org.openqa.selenium.ie.InternetExplorerDriverLogLevel;
import org.openqa.selenium.ie.InternetExplorerDriverService;

public class InternetExplorerDriverLogger extends DriverLogger {
    public static InternetExplorerDriverLogger enable() {
        InternetExplorerDriverLogger logger = new InternetExplorerDriverLogger();
        logger.ensureEnabled();
        return logger;
    }

    public static InternetExplorerDriverLogger all() {
        InternetExplorerDriverLogger logger = new InternetExplorerDriverLogger();
        logger.setLevel(InternetExplorerDriverLogLevel.TRACE);
        return logger;
    }

    public void setLevel(InternetExplorerDriverLogLevel level) {
        System.setProperty(getLogLevelProperty(), level.name());
        ensureOutput();
    }

    public InternetExplorerDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : InternetExplorerDriverLogLevel.valueOf(logLevel);
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
