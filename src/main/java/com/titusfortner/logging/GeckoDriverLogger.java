package com.titusfortner.logging;

import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
public class GeckoDriverLogger {
    private final String fileProperty = FirefoxDriver.SystemProperty.BROWSER_LOGFILE;

    public void enable() {
        setProperty("/dev/stderr");
    }

    public void disable() {
        setProperty("/dev/null");
    }

    public void setFile(File fileName) {
        setProperty(fileName.getAbsolutePath());
    }

    public File getFile() {
        String path = System.getProperty(fileProperty);
        if (path == null) {
            return null;
        } else {
            return new File(path);
        }
    }

    private void setProperty(String property) {
        System.setProperty(fileProperty, property);
    }
}
