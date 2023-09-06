package com.titusfortner.logging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class SeleniumFilter implements Filter {

    private final List<String> logAllowedList;
    private final List<String> logBlockList;

    public SeleniumFilter() {
        logAllowedList = new ArrayList<>(Collections.singletonList("org.openqa.selenium"));
        logBlockList = new ArrayList<>();
    }

    public void addAllowed(String allowed) {
        Objects.requireNonNull(allowed);
        logAllowedList.add(allowed);
    }

    public List<String> getLogAllowedList() {
        return logAllowedList;
    }

    public void addBlocked(String blocked) {
        Objects.requireNonNull(blocked);
        logBlockList.add(blocked);
    }

    public List<String> getLogBlockList() {
        return logBlockList;
    }

    public boolean isLoggable(LogRecord record) {
        String loggerName = record.getLoggerName();
        return getLogAllowedList().stream().filter(loggerName::contains)
                .anyMatch(_a -> getLogBlockList().stream().noneMatch(loggerName::contains));
    }
}
