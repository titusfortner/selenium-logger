package com.titusfortner.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

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
