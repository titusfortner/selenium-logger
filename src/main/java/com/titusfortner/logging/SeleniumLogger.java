package com.titusfortner.logging;

import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SeleniumLogger {
    private final List<String> loggedClasses = new ArrayList<>(Arrays.asList(RemoteWebDriver.class.getName(), SeleniumManager.class.getName()));
    private Handler handler = new ConsoleHandler();
    private Level level = Level.INFO;
    private Formatter formatter = new SeleniumFormatter();
    private File file;
    public final Logger rootLogger = Logger.getLogger("");
    private final GeckoDriverLogger geckoDriverLogger = new GeckoDriverLogger();

    public SeleniumLogger() {
        Arrays.stream(rootLogger.getHandlers()).forEach(rootLogger::removeHandler);
        updateLogger();
    }

    public GeckoDriverLogger geckodriver() {
        return geckoDriverLogger;
    }

    public void addLoggedClass(String className) {
        Objects.requireNonNull(className);
        loggedClasses.add(className);
        updateLogger();
    }

    public void removeLoggedClass(String className) {
        Objects.requireNonNull(className);
        Logger logger = Logger.getLogger(className);
        Arrays.stream(logger.getHandlers()).forEach(logger::removeHandler);

        loggedClasses.remove(className);
    }

    public List<String> getLoggedClasses() {
        return loggedClasses;
    }

    public void setHandler(Handler handler) {
        Objects.requireNonNull(handler);
        this.handler = handler;
        updateLogger();
    }

    public Handler getHandler() {
        handler.setLevel(getLevel());
        handler.setFormatter(getFormatter());
        return handler;
    }

    public void setLevel(Level level) {
        Objects.requireNonNull(level);
        this.level = level;
        updateLogger();
    }

    public Level getLevel() {
        return level;
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
        setFileOutput(file, getFormatter());
    }

    public void setFileOutput(File file, Formatter formatter) {
        Objects.requireNonNull(file);
        Objects.requireNonNull(formatter);
        this.file = file;
        try {
            Handler handler = new FileHandler(file.toString());
            handler.setFormatter(formatter);
            setHandler(handler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File getFileOutput() {
        return file;
    }

    private void updateLogger() {
        getLoggedClasses().forEach(logName -> {
            Logger logger = Logger.getLogger(logName);
            logger.setLevel(getLevel());
            Arrays.stream(logger.getHandlers()).forEach(logger::removeHandler);
            logger.addHandler(getHandler());
        });
    }
}
