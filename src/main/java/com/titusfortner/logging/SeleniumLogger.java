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
import java.util.logging.SimpleFormatter;

public class SeleniumLogger {
    public static final String FORMAT = "%1$tF %1$tT %4$s - %5$s %n";
    private final List<String> loggedClasses = new ArrayList<>(List.of(RemoteWebDriver.class.getName(), SeleniumManager.class.getName()));
    private Handler handler;
    private Level level = Level.INFO;
    private File file;
    public final Logger rootLogger = Logger.getLogger("");

    public SeleniumLogger() {
        if (System.getProperty("java.util.logging.SimpleFormatter.format") == null) {
            System.setProperty("java.util.logging.SimpleFormatter.format", FORMAT);
        }
    }

    public void addLoggedClass(String className) {
        Objects.requireNonNull(className);
        loggedClasses.add(className);
    }

    public void removeLoggedClass(String className) {
        Objects.requireNonNull(className);
        Logger.getLogger(className).setLevel(Level.INFO);

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
        if (handler == null) {
            this.handler = new ConsoleHandler();
        }
        handler.setLevel(getLevel());
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

    public void setFileOutput(File file) {
        setFileOutput(file, new SimpleFormatter());
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

        Arrays.stream(rootLogger.getHandlers()).forEach(rootLogger::removeHandler);
    }
}
