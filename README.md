# Selenium Logger

The most useful information for Selenium users gets quickly buried in the logs by default.

This package is intended to provide a means to quickly get the most useful information...

This code is only one example of what can be done.

If you have...
* **a bug** — let me know by [Creating an Issue](https://github.com/titusfortner/selenium-logger/issues/new)!
* **an improvement** — if it helps everyone, [Create a PR](https://github.com/titusfortner/selenium-logger/pulls)!
* **a different opinion** —  if this library does something that you don't like, copy/paste the code to your own project and edit it however you want!
* **a different implementation** — if there's a better way to do things than this library, create your own! (I'll even help you get it published if you want)

## Usage

### Logger level

Logs are set to `Level.INFO` by default. Almost everything in Selenium is currently logged with `Level.FINE`.
To change the level to `FINE`:

```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.setLevel(Level.FINE))
```

### Logger output

By default, logs are sent to the console in `stderr`.
if you want to store the logs in a file, add this to your code:

```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
File logFile = new File("/path/to/selenium.log");
seleniumLogger.setFileOutput(file)
```

### Logger classes

By default, this library logs everything in the `RemoteWebDriver.class` and `SeleniumManager.class`

The most useful information that gets logged by Selenium is in the `RemoteWebDriver.class`. This is what records all input and output
sent to the driver or grid.

The `SeleniumManager.class` will be increasingly important as Selenium tries to make sure that drivers are properly handled in all contexts.

#### Add a class

If there's an additional class that you want to get logger information from you can quickly add it with this library.
Note that this doesn't have to be a Selenium class, this can be any class in any library.

```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.addLoggedClass(ChromeDriver.class.getName());
```

#### Remove a class

If you only want to see details about RemoteWebDriver, you can remove the `SeleniumManager.class` logging by doing:

```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.removeLoggedClass(SeleniumManager.class.getName());
```
