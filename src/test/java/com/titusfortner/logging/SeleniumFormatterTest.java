package com.titusfortner.logging;

import com.google.common.io.Resources;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.HasFullPageScreenshot;
import org.openqa.selenium.print.PrintOptions;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.SimpleFormatter;

public class SeleniumFormatterTest extends BaseTest {
    private final SeleniumLogger seleniumLogger = new SeleniumLogger();

    @Test
    public void setFormatter() {
        seleniumLogger.enable().setFormatter(new SimpleFormatter());

        logsWarning();

        String fine = "chrome banana not available for download";
        Assertions.assertTrue(getOutput().contains(fine));
    }

    @Test
    public void getFormatter() {
        Assertions.assertSame(seleniumLogger.getFormatter().getClass(), SeleniumFormatter.class);
    }

    @Test
    public void defaultFormattingTruncates() throws IOException {
        SeleniumLogger.enable();

        driver = new FirefoxDriver();
        driver.get("https://www.selenium.dev");

        ((PrintsPage) driver).print(new PrintOptions());
        ((HasFullPageScreenshot) driver).getFullPageScreenshotAs(OutputType.BASE64);

        WebElement element = driver.findElement(By.tagName("body"));
        ((JavascriptExecutor) driver).executeScript(isDisplayedScript(), element);

        Assertions.assertTrue(getOutput().contains("printPage response suppressed"));
        Assertions.assertTrue(getOutput().contains("fullPageScreenshot response suppressed"));
        Assertions.assertTrue(getOutput().contains("{script=/* isDisplayed */return (function(){ret..."));
    }

    @Test
    public void removeTruncation() throws IOException {
        SeleniumLogger.enable().setFullLogMessages(true);

        driver = new FirefoxDriver();

        WebElement element = driver.findElement(By.tagName("body"));
        ((JavascriptExecutor) driver).executeScript(isDisplayedScript(), element);

        Assertions.assertFalse(getOutput().contains("{script=/* isDisplayed */return (function(){ret..."));
    }

    private String isDisplayedScript() throws IOException {
        URL resource = getClass().getResource("/org/openqa/selenium/remote/isDisplayed.js");
        String function = Resources.toString(Objects.requireNonNull(resource), StandardCharsets.UTF_8);
        String format = "/* isDisplayed */return (%s).apply(null, arguments);";
        return String.format(format, function);
    }
}
