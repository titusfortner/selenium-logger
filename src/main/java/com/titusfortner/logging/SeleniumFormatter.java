package com.titusfortner.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        String source = "";
        if (record.getSourceClassName() != null) {
            try {
                source = record.getSourceClassName().contains("org.openqa.selenium") ? "Selenium [" : "[";
                String fullName = record.getSourceClassName().replaceAll("\\$.*", "");
                source += Class.forName(fullName).getSimpleName();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String methodName = record.getSourceMethodName();
            if (methodName != null && !methodName.contains("lambda") && !methodName.equals("log")) {
                source += " " + record.getSourceMethodName();
            }
        } else {
            source = record.getLoggerName();
        }
        String message = formatMessage(record);

        if (message == null) {
            // See https://github.com/SeleniumHQ/selenium/pull/12853
            message = record.getThrown().getCause().getMessage();
        }

        // See https://github.com/SeleniumHQ/selenium/pull/12866
        if (Boolean.getBoolean("webdriver.remote.shorten_log_messages")) {
            message = filterOutBase64(message);
            message = filterOutProfile(message);
        }

        String throwable = "";
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            record.getThrown().printStackTrace(pw);
            pw.close();
            throwable = sw.toString();
        }
        String format = "%1$tF %1$tT %4$s %2$s] %5$s %n";
        return String.format(format, new Date(record.getMillis()), source,
                record.getLoggerName(), record.getLevel(), message, throwable);
    }

    private String filterOutBase64(String message) {
        String regex = "Executed: (printPage|fullPageScreenshot) \\(Response: SessionID: .*, State: success, Value:(.*)";
        Pattern patternBase64 = Pattern.compile(regex);
        Matcher matcherBase64 = patternBase64.matcher(message);
        if (matcherBase64.find()) {
            String command = matcherBase64.group(1);
            String base64 = matcherBase64.group(2);
            return message.replace(base64, String.format(" *%s response suppressed*)", command));
        }
        return message;
    }

    private String filterOutProfile(String message) {
        Pattern patternProfile = Pattern.compile("\\{profile=(.*)}");
        Matcher matcherProfile = patternProfile.matcher(message);
        if (matcherProfile.find()) {
            String group = matcherProfile.group(1);
            if (group.length() > 300) {
                return message.replace(group, " *profile value suppressed*}");
            }
        }
        return message;
    }
}
