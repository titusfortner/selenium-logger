package com.titusfortner.logging;

import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chromium.ChromiumDriverLogLevel;

public class ChromeDriverLogger extends ChromiumDriverLogger{
    public static ChromeDriverLogger enable() {
        ChromeDriverLogger logger = new ChromeDriverLogger();
        logger.ensureEnabled();
        return logger;
    }

    public static ChromeDriverLogger all() {
        ChromeDriverLogger logger = new ChromeDriverLogger();
        logger.setLevel(ChromiumDriverLogLevel.ALL);
        return logger;
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
