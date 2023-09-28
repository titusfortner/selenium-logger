package com.titusfortner.logging;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleniumLogger {
    private Handler handler = new ConsoleHandler();
    private Level level = Level.INFO;
    private Filter filter = new SeleniumFilter();
    private Formatter formatter = new SeleniumFormatter();
    private File file;
    public final Logger rootLogger = Logger.getLogger("");
    private final GeckoDriverLogger geckoDriverLogger = new GeckoDriverLogger();

    /**
     * This turns on logger level to FINE
     * @return a Selenium Logger
     */
    public static SeleniumLogger enable() {
        SeleniumLogger logger = new SeleniumLogger();
        logger.setLevel(Level.FINE);
        return logger;
    }

    public static SeleniumLogger enable(String name, String... names) {
        SeleniumLogger logger = SeleniumLogger.enable();
        if (name != null) {
            logger.filter(name, names);
        }
        return logger;
    }

    public static SeleniumLogger all() {
        SeleniumLogger logger = new SeleniumLogger();
        SeleniumFilter seleniumFilter = new SeleniumFilter();
        seleniumFilter.addAllowed("");

        logger.setFilter(seleniumFilter);
        logger.setLevel(Level.ALL);
        return logger;
    }

    public SeleniumLogger() {
        updateLogger();
    }

    public void setLevel(Level level) {
        Objects.requireNonNull(level);
        this.level = level;
        updateLogger();
    }

    public Level getLevel() {
        return level;
    }

    public void disable() {
        this.level = Level.OFF;
        updateLogger();
    }

    public void setFormatter(Formatter formatter) {
        Objects.requireNonNull(formatter);
        this.formatter = formatter;
        updateLogger();
    }

    public Formatter getFormatter() {
        return formatter;
    }

    public void setFileOutput(File file) {
        Objects.requireNonNull(file);
        this.file = file;
        try {
            handler = new FileHandler(file.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updateLogger();
    }

    public File getFileOutput() {
        return file;
    }

    public void filter(String name, String... names) {
        Objects.requireNonNull(name);
        SeleniumFilter filter = new SeleniumFilter();
        filter.clear();
        filter.addAllowed(name);
        Arrays.stream(names).forEach(filter::addAllowed);
        setFilter(filter);
    }

    public void filterOut(String name, String... names) {
        Objects.requireNonNull(name);
        SeleniumFilter filter = new SeleniumFilter();
        filter.addBlocked(name);
        Arrays.stream(names).forEach(filter::addBlocked);
        setFilter(filter);
    }

    public void setFilter(Filter filter) {
        Objects.requireNonNull(filter);
        this.filter = filter;
        updateLogger();
    }

    public Filter getFilter() {
        return filter;
    }

    private void updateLogger() {
        Arrays.stream(rootLogger.getHandlers()).forEach(rootLogger::removeHandler);
        rootLogger.setLevel(getLevel());
        handler.setLevel(getLevel());
        handler.setFormatter(getFormatter());
        handler.setFilter(getFilter());
        rootLogger.addHandler(handler);
    }
}
