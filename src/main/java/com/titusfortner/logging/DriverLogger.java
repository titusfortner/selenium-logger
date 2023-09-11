package com.titusfortner.logging;

import org.openqa.selenium.remote.service.DriverService;

import java.io.File;

public abstract class DriverLogger {
    protected abstract String getLogProperty();
    protected abstract void ensureLevel();

    public void disable() {
        System.setProperty(getLogProperty(), DriverService.LOG_NULL);
    }

    public String getOutput() {
        return System.getProperty(getLogProperty());
    }

    public void setFile(File fileName) {
        System.setProperty(getLogProperty(), fileName.getAbsolutePath());
        ensureLevel();
    }

    public File getFile() {
        String path = getOutput();
        return path == null || !new File(path).isFile() ? null : new File(path);
    }

    protected void ensureOutput() {
        if (getOutput() == null) {
            System.setProperty(getLogProperty(), DriverService.LOG_STDERR);
        }
    }

    protected void ensureEnabled() {
        ensureOutput();
        ensureLevel();
    }
}
