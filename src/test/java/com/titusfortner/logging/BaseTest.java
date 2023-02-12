package com.titusfortner.logging;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
public class BaseTest {
    protected WebDriver driver;
    protected SeleniumLogger seleniumLogger;
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();
    private static final PrintStream originalErr = System.err;

    @BeforeEach
    public void setup() {
        System.setErr(new PrintStream(err));
        seleniumLogger = new SeleniumLogger();
    }

    @AfterEach
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @AfterAll
    public static void restoreErr() {
        System.setErr(originalErr);
    }

    public String getOutput() {
        return err.toString();
    }
}
