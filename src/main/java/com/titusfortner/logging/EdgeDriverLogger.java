package com.titusfortner.logging;

import org.openqa.selenium.edge.EdgeDriverService;

public class EdgeDriverLogger extends ChromiumDriverLogger{

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
