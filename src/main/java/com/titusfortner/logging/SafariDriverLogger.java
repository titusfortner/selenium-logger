package com.titusfortner.logging;

import com.google.common.annotations.Beta;
import org.openqa.selenium.safari.SafariDriverService;

import java.io.File;
import java.util.*;

public class SafariDriverLogger {
    public void enable() {
        System.setProperty(SafariDriverService.SAFARI_DRIVER_LOGGING, "true");
    }

    public File getDirectory() {
        String path = System.getProperty("user.home") + "/Library/Logs/com.apple.WebDriver/";
        return new File(path);
    }

    public List<File> getList() {
        File[] files = getDirectory().listFiles();
        return files == null ? null : Arrays.asList(Objects.requireNonNull(files));
    }

    @Beta
    public File getFile() {
        return getList().stream().max(Comparator.comparingLong(File::lastModified)).orElse(null);
    }
}