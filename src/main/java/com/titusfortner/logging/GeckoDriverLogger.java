package com.titusfortner.logging;

import org.openqa.selenium.firefox.FirefoxDriverLogLevel;
import org.openqa.selenium.firefox.GeckoDriverService;

public class GeckoDriverLogger extends DriverLogger{
    public static GeckoDriverLogger enable() {
        GeckoDriverLogger logger = new GeckoDriverLogger();
        logger.ensureEnabled();
        return logger;
    }

    public static GeckoDriverLogger all() {
        GeckoDriverLogger logger = new GeckoDriverLogger();
        logger.setLevel(FirefoxDriverLogLevel.TRACE);
        return logger;
    }

    public void setLevel(FirefoxDriverLogLevel level) {
        System.setProperty(getLogLevelProperty(), level.name());
        ensureOutput();
    }

    public FirefoxDriverLogLevel getLevel() {
        String logLevel = System.getProperty(getLogLevelProperty());
        return logLevel == null ? null : FirefoxDriverLogLevel.valueOf(logLevel);
    }

    public void setTruncate(Boolean truncate) {
        System.setProperty(getTruncateProperty(), String.valueOf(!truncate));
        ensureLevel();
        ensureOutput();
    }

    protected void ensureLevel() {
        if (getLevel() == null) {
            setLevel(FirefoxDriverLogLevel.INFO);
        }
    }

    protected String getLogProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_PROPERTY;
    }

    private String getLogLevelProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_LEVEL_PROPERTY;
    }

    private String getTruncateProperty() {
        return GeckoDriverService.GECKO_DRIVER_LOG_NO_TRUNCATE;
    }
}
