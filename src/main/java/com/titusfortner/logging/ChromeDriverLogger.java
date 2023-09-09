package com.titusfortner.logging;

import org.openqa.selenium.chrome.ChromeDriverService;

public class ChromeDriverLogger extends ChromiumDriverLogger{

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
