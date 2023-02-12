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
    private final List<String> loggedClasses = new ArrayList<>(List.of(RemoteWebDriver.class.getName(), SeleniumManager.class.getName()));
    private Handler handler = new ConsoleHandler();
    private Level level = Level.INFO;
    private String format = "%1$tF %1$tT %4$s - %5$s %n";
    private File file;
    public final Logger rootLogger = Logger.getLogger("");

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
        handler.setLevel(getLevel());
        return handler;
    }

    public void setLevel(Level level) {
        Objects.requireNonNull(level);
        this.level = level;
        this.handler.setLevel(level);
        updateLogger();
    }

    public Level getLevel() {
        return level;
    }

    public void setFormat(String format) {
        this.format = format;
        System.setProperty("java.util.logging.SimpleFormatter.format", format);
    }

    public String getFormat() {
        return format;
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
        System.setProperty("java.util.logging.SimpleFormatter.format", getFormat());

        getLoggedClasses().forEach(logName -> {
            Logger logger = Logger.getLogger(logName);
            logger.setLevel(getLevel());
            Arrays.stream(logger.getHandlers()).forEach(logger::removeHandler);
            logger.addHandler(getHandler());
        });

        Arrays.stream(rootLogger.getHandlers()).forEach(handler -> {
            handler.setLevel(getLevel());
        });
    }
}
