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
            // Throwable can strip out message from cause
            // https://github.com/SeleniumHQ/selenium/pull/12853
            message = record.getThrown().getCause().getMessage();
        }

        Pattern patternBase64Encoded = Pattern.compile("\"body\":\".*\",\"base64Encoded\":true");
        Matcher matcherBase64Encoded = patternBase64Encoded.matcher(message);
        if (matcherBase64Encoded.find()) {
            message = message.replaceAll("\"body\":\".*\",\"base64Encoded\":true", "\"body\":<snip>");
        } else {
            Pattern patternBody = Pattern.compile("\"body\": \"(.*)\"");
            Matcher matcherBody = patternBody.matcher(message);
            if (matcherBody.find()) {
                String matched = matcherBody.group(1);
                if (matched.length() > 2000) {
                    message = message.replace(matched, "<snip>");
                }
            }}

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
}
