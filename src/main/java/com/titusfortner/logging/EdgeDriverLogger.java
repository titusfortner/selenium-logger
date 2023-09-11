package com.titusfortner.logging;

import org.openqa.selenium.chromium.ChromiumDriverLogLevel;
import org.openqa.selenium.edge.EdgeDriverService;

public class EdgeDriverLogger extends ChromiumDriverLogger{
    public static EdgeDriverLogger enable() {
        EdgeDriverLogger logger = new EdgeDriverLogger();
        logger.ensureEnabled();
        return logger;
    }

    public static EdgeDriverLogger all() {
        EdgeDriverLogger logger = new EdgeDriverLogger();
        logger.setLevel(ChromiumDriverLogLevel.ALL);
        return logger;
    }

    protected String getLogProperty() {
        return EdgeDriverService.EDGE_DRIVER_LOG_PROPERTY;
    }

    protected String getReadableTimestampProperty() {
        return EdgeDriverService.EDGE_DRIVER_READABLE_TIMESTAMP;
    }

    protected String getLogLevelProperty() {
        return EdgeDriverService.EDGE_DRIVER_LOG_LEVEL_PROPERTY;
    }

    protected String getAppendLogProperty() {
        return EdgeDriverService.EDGE_DRIVER_APPEND_LOG_PROPERTY;
    }
}
