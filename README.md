# Selenium Logger

The most useful information for Selenium users gets quickly buried in the logs by default.

This package is intended to provide a means to quickly get the most useful information...

It provides a standard Java logging wrapper to get Selenium code execution logs, as well
as providing an interface for managing the logs from the drivers.

If you have...
* **a bug** — let me know by [Creating an Issue](https://github.com/titusfortner/selenium-logger/issues/new)!
* **an improvement** — if it helps everyone, [Create a PR](https://github.com/titusfortner/selenium-logger/pulls)!
* **a different opinion** —  if this library does something that you don't like, copy/paste the code to your own project and edit it however you want!
* **a different implementation** — if there's a better way to do things than this library, create your own! (I'll even help you get it published if you want)

## Installation

For Maven users, add this dependency to `pom.xml`
```
<dependency>
  <groupId>com.titusfortner</groupId>
  <artifactId>selenium-logger</artifactId>
  <version>1.1</version>
</dependency>
```

## Selenium Usage

By default, Java log level is set to `Level.INFO` and the default output is `System.err`. 

### Level

Almost everything in Selenium is currently logged with `Level.FINE`, so using `enable()` changes the level to `FINE`:
```java
SeleniumLogger.enable();
```

To set a specific level (e.g., you want to avoid info logs and just see warnings)
```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.setLevel(Level.WARNING);
```

### Output

By default, logs are sent to the console in `System.err`.
If you want to store the logs in a file, add this to your code:

```java
SeleniumLogger seleniumLogger = SeleniumLogger.enable();
File logFile = new File("/path/to/selenium-log.txt");
seleniumLogger.setFileOutput(file)
```

### Filtering

Currently, enabling logging with this project will only log for RemoteWebDriver and SeleniumManager classes. 
For more control, use a filter to generate an allow-list of classes to log based on logger name:
```java
SeleniumLogger seleniumLogger = SeleniumLogger.enable();
seleniumLogger.filter("RemoteWebDriver");
```

### Advanced

* Disable all logging with `SeleniumLogger.disable()`
* Turn on logging for all classes, all levels with `SeleniumLogger.all()`
* Create and use your own formatter with `seleniumLogger.setFormatter()`
* Create and use your own filter with `seleniumLogger.setFilter()`


## Driver Usage

This functionality was not fully supported in Chrome and Edge before Selenium 4.12.1

Geckodriver logs used to be turned on and sent to console by default, which created a lot of noise.
As of Selenium 4.9.1, logs are now turned off by default, so they must be enabled.

### Enable

These methods will log driver output at the `INFO` level and output to `System.err`
```java
new ChromeDriverLogger().enable();
new EdgeDriverLogger().enable();
new GeckoDriverLogger().enable();
new InternetExplorerDriverLogger().enable();
```

### Level
Setting a level ensures output is sent to `System.err`, without needing to call `enable()`.

To change to a custom level:
```java
new ChromeDriverLogger().setLevel(ChromiumDriverLogLevel.DEBUG);
new EdgeDriverLogger().setLevel(ChromiumDriverLogLevel.DEBUG);
new GeckoDriverLogger().setLevel(FirefoxDriverLogLevel.DEBUG);
new InternetExplorerDriverLogger().setLevel(InternetExplorerDriverLogLevel.DEBUG);
```

You can also log everything with one method:
```java
new ChromeDriverLogger().all();
new EdgeDriverLogger().all();
new GeckoDriverLogger().all();
new InternetExplorerDriverLogger().all();
```

### Output
Sending output to a file ensures level is set to level `INFO`, without needing to call `enable()`.

To send output to a file:
```java
new ChromeDriverLogger().setFile(new File("chromedriver.log"));
new EdgeDriverLogger().setFile(new File("edgedriver.log"));
new GeckoDriverLogger().setFile(new File("geckodriver.log"));
new InternetExplorerDriverLogger().setFile(new File("iedriver.log"));
```

### Browser Specific Support

#### Chromium

* Append to a log file instead of rewriting it with `appendToLog()`
* Set readable timestamps with `setReadableTimestamp()`

#### Firefox

Firefox truncates long lines in the log by default. This can be turned
off with: `new Geckodriver().setTruncate(false)`

#### Safari

Safari is special. It does not log to console, only to a file. The
file has a unique name per session and is stored in `"~/Library/Logs/com.apple.WebDriver/"`.
You also cannot change the log level, you get whatever Safari gives you.

* To toggle on this logging you can do: `new SafariDriverLogger().enable()`.
* To get a File object with absolute path to the directory: `getDirectory()`
* To get a list of all files in that directory; `getList()`
* To get the last file in the directory (most likely to be the logs you are looking for): `getFile()`
