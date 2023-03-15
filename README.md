# Selenium Logger

The most useful information for Selenium users gets quickly buried in the logs by default.

This package is intended to provide a means to quickly get the most useful information...

This code is only one example of what can be done.

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

## Usage

### Logger level

By default, logs are set to `Level.INFO`. Almost everything in Selenium is currently logged with `Level.FINE`.
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
seleniumLogger.setLevel(Level.FINE))
File logFile = new File("/path/to/selenium-log.txt");
seleniumLogger.setFileOutput(file)
```

### Geckodriver

By default, Geckodriver sends a lot of logging information to the console.
To just turn it off:

```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.geckodriver().disable();
```

Turning on Selenium Logging with this library *turns it off* by default.
Here's an example of setting Selenium Logging to `FINE`, turning geckodriver logs back on,
then sending it to a file instead of the default `stderr`
```java
SeleniumLogger seleniumLogger = new SeleniumLogger();
seleniumLogger.setLevel(Level.FINE))
seleniumLogger.geckodriver().enable();
seleniumLogger.geckodriver().setFile(new File("/path/to/geckodriver-log.txt"));
```

Note that this library can not currently set the log Level for geckodriver,
and setting it in the Options does not appear to have an effect right now.
There is an open Selenium Issue (https://github.com/SeleniumHQ/selenium/issues/11410) that will allow this.

### Chromedriver

At this time, this library does not directly affect chromedriver logs or output. There is an open issue
in Selenium (https://github.com/SeleniumHQ/selenium/issues/11643) that will allow better support for
this.

## Examples

### Chrome

By default (without Logger) in Selenium 4.8.1:

```java
WebDriver driver = new ChromeDriver();
driver.quit();
```

```text
Starting ChromeDriver 111.0.5563.64 (c710e93d5b63b7095afe8c2c17df34408078439d-refs/branch-heads/5563@{#995}) on port 43490
Only local connections are allowed.
Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
ChromeDriver was started successfully.
Mar 15, 2023 12:26:07 PM org.openqa.selenium.devtools.CdpVersionFinder findNearestMatch
WARNING: Unable to find an exact match for CDP version 111, so returning the closest version found: 110
```

With Logger set to `FINE`

```java
new SeleniumLogger().setLevel(Level.FINE);
WebDriver driver = new ChromeDriver();
driver.quit();
```

```text
2023-03-15 12:27:34 FINE - Executing: newSession [null, newSession {capabilities=[Capabilities {browserName: chrome, goog:chromeOptions: {args: [], extensions: []}}], desiredCapabilities=Capabilities {browserName: chrome, goog:chromeOptions: {args: [], extensions: []}}}] 
Starting ChromeDriver 111.0.5563.64 (c710e93d5b63b7095afe8c2c17df34408078439d-refs/branch-heads/5563@{#995}) on port 43174
Only local connections are allowed.
Please see https://chromedriver.chromium.org/security-considerations for suggestions on keeping ChromeDriver safe.
ChromeDriver was started successfully.
2023-03-15 12:27:36 FINE - Executed: (Response: SessionID: 95cfaf4a83ea13a33001226e2b469cea, Status: 0, Value: {acceptInsecureCerts=false, browserName=chrome, browserVersion=111.0.5563.64, chrome={chromedriverVersion=111.0.5563.64 (c710e93d5b63b7095afe8c2c17df34408078439d-refs/branch-heads/5563@{#995}), userDataDir=/var/folders/fx/nlx77ccs72g808pr1994_xtw0000gn/T/.com.google.Chrome.XtAtA5}, goog:chromeOptions={debuggerAddress=localhost:55994}, networkConnectionEnabled=false, pageLoadStrategy=normal, platformName=mac os x, proxy=Proxy(), setWindowRect=true, strictFileInteractability=false, timeouts={implicit=0, pageLoad=300000, script=30000}, unhandledPromptBehavior=dismiss and notify, webauthn:extension:credBlob=true, webauthn:extension:largeBlob=true, webauthn:extension:minPinLength=true, webauthn:extension:prf=true, webauthn:virtualAuthenticators=true}) 
2023-03-15 12:27:36 FINE - Executing: quit [95cfaf4a83ea13a33001226e2b469cea, quit {}] 
2023-03-15 12:27:37 FINE - Executed: (Response: SessionID: 95cfaf4a83ea13a33001226e2b469cea, Status: 0, Value: null) 
```

### Firefox

As mentioned above, Geckodriver sends quite a bit of logging output to `stderr` by default:

```java
WebDriver driver = new FirefoxDriver();
driver.quit();
```

```text
1678903441428	geckodriver	INFO	Listening on 127.0.0.1:13240
1678903441680	mozrunner::runner	INFO	Running command: "/Applications/Firefox.app/Contents/MacOS/firefox-bin" "--marionette" "--remote-debugging-port" "2018" "--re ... //[::1]:2018/" "-foreground" "-no-remote" "-profile" "/var/folders/fx/nlx77ccs72g808pr1994_xtw0000gn/T/rust_mozprofilentBFjM"
console.warn: services.settings: Ignoring preference override of remote settings server
console.warn: services.settings: Allow by setting MOZ_REMOTE_SETTINGS_DEVTOOLS=1 in the environment
1678903442198	Marionette	INFO	Marionette enabled
1678903442317	Marionette	INFO	Listening on port 56506
console.error: "Warning: unrecognized command line flag -remote-allow-hosts\n"
console.error: "Warning: unrecognized command line flag -remote-allow-origins\n"
Read port: 56506
WebDriver BiDi listening on ws://127.0.0.1:2018
1678903442816	RemoteAgent	WARN	TLS certificate errors will be ignored for this session
console.warn: SearchSettings: "get: No settings file exists, new profile?" (new NotFoundError("Could not open the file at /var/folders/fx/nlx77ccs72g808pr1994_xtw0000gn/T/rust_mozprofilentBFjM/search.json.mozlz4", (void 0)))
DevTools listening on ws://127.0.0.1:2018/devtools/browser/dbd50006-77a2-4aff-a6eb-309542d2a059
1678903444556	RemoteAgent	INFO	Perform WebSocket upgrade for incoming connection from 127.0.0.1:56522
1678903444559	CDP	ERROR	Invalid browser preferences for CDP. Set "fission.webContentIsolationStrategy"to 0 and "fission.bfcacheInParent" to false before Firefox starts.
1678903444601	Marionette	INFO	Stopped listening on port 56506
console.error: services.settings: 
  main/quicksuggest Signature failed  Error: Shutdown, aborting read-only worker requests.
console.error: (new Error("Shutdown, aborting read-only worker requests.", "resource://services-settings/RemoteSettingsWorker.jsm", 143))
console.error: (new Error("Shutdown, aborting read-only worker requests.", "resource://services-settings/RemoteSettingsWorker.jsm", 143))
console.error: (new Error("Shutdown, aborting read-only worker requests.", "resource://services-settings/RemoteSettingsWorker.jsm", 143))
WARNING: A blocker encountered an error while we were waiting.
          Blocker:  Waiting for ping task
          Phase: TelemetryController: Waiting for pending ping activity
          State: (none)
WARNING: Error: Phase "profile-before-change" is finished, it is too late to register completion condition "OS.File: flush I/O queued before profileBeforeChange"
WARNING: addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:727:15
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:523:26
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:458:15
setupShutdown@resource://gre/modules/osfile/osfile_async_front.jsm:1548:28
@resource://gre/modules/osfile/osfile_async_front.jsm:1568:16
@resource://gre/modules/osfile.jsm:12:30
@resource://gre/modules/TelemetryStorage.sys.mjs:10:28
_checkPendingPings@resource://gre/modules/TelemetrySend.sys.mjs:863:17
setup@resource://gre/modules/TelemetrySend.sys.mjs:803:18
setup@resource://gre/modules/TelemetrySend.sys.mjs:241:30
setupTelemetry/this._delayedInitTask<@resource://gre/modules/TelemetryControllerParent.sys.mjs:829:36
observe@resource://gre/modules/AsyncShutdown.sys.mjs:576:16

WARNING: A blocker encountered an error while we were waiting.
          Blocker:  Waiting for ping task
          Phase: TelemetryController: Waiting for pending ping activity
          State: (none)
WARNING: Error: Phase "profile-before-change" is finished, it is too late to register completion condition "OS.File: flush I/O queued before profileBeforeChange"
WARNING: addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:727:15
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:523:26
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:458:15
setupShutdown@resource://gre/modules/osfile/osfile_async_front.jsm:1548:28
@resource://gre/modules/osfile/osfile_async_front.jsm:1568:16
@resource://gre/modules/osfile.jsm:12:30
@resource://gre/modules/TelemetryStorage.sys.mjs:10:28
_checkPendingPings@resource://gre/modules/TelemetrySend.sys.mjs:863:17
setup@resource://gre/modules/TelemetrySend.sys.mjs:803:18
setup@resource://gre/modules/TelemetrySend.sys.mjs:241:30
setupTelemetry/this._delayedInitTask<@resource://gre/modules/TelemetryControllerParent.sys.mjs:829:36
observe@resource://gre/modules/AsyncShutdown.sys.mjs:576:16

WARNING: A blocker encountered an error while we were waiting.
          Blocker:  Waiting for ping task
          Phase: TelemetryController: Waiting for pending ping activity
          State: (none)
WARNING: Error: Phase "profile-before-change" is finished, it is too late to register completion condition "OS.File: flush I/O queued before profileBeforeChange"
WARNING: addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:727:15
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:523:26
addBlocker@resource://gre/modules/AsyncShutdown.sys.mjs:458:15
setupShutdown@resource://gre/modules/osfile/osfile_async_front.jsm:1548:28
@resource://gre/modules/osfile/osfile_async_front.jsm:1568:16
@resource://gre/modules/osfile.jsm:12:30
@resource://gre/modules/TelemetryStorage.sys.mjs:10:28
_checkPendingPings@resource://gre/modules/TelemetrySend.sys.mjs:863:17
setup@resource://gre/modules/TelemetrySend.sys.mjs:803:18
setup@resource://gre/modules/TelemetrySend.sys.mjs:241:30
setupTelemetry/this._delayedInitTask<@resource://gre/modules/TelemetryControllerParent.sys.mjs:829:36
observe@resource://gre/modules/AsyncShutdown.sys.mjs:576:16

WARNING: A blocker encountered an error while we were waiting.
          Blocker:  TelemetryController: shutting down
          Phase: profile-before-change-telemetry
          State: Error getting state: Error: Phase "profile-before-change" is finished, it is too late to register completion condition "OS.File: flush I/O queued before profileBeforeChange" at addBlocker@resource://gre/modules/AsyJavaScript necrSrhourt:d orwens.osuyrsc.em:j/s/:g7r2e7/:m1o5d
addBlocker@resouulrecse/:A/s/ygnrceS/hmuotdduolwens./sAyssy.nmcjSsh,u tldionwen .7s2y7s:. mEjrsr:o5r2:3:26
 aPdhdaBsleo c"kperro@frielseo-ubrecfeo:r/e/-gcrhea/nmgoed"u liess /fAisnyinschSehdu,t diotw ni.ss ytso.om jlsa:t4e5 8t:o1 5r
setupShutdeogwins@treers ocuormcpel:e/t/igorne /cmoondduilteiso/no s"fOiSl.eF/iolsef:i lfel_uasshy nIc/_Of rqounetu.ejds mb:e1f5ore profileBefor4e8C:h2a8n
@resource://grge/em"o
JavaScript errodr:u lreess/oousrfciel:e///ogsre/mofdiullee_sa/sAysnycn_cfSrhounttd.ojwsnm.:s1y5s6.8m:j1s6,
@resource://gre /mloidnuel e7s2/7o:s fEirlreo.rj:s mP:h1a2s:e3 0"
@resource:p/r/ogfriel/em-obdeulefso/rTee-lcehmaentgrey"S tiosr afgien.issyhse.dm,j si:t1 0i:s2 8t
_checkPendingPoingso@ rleastoeu rtcoe :r/e/ggirset/emro dcuolmepsl/eTteiloenm ectornydSietnido.ns y"sO.Sm.jFsi:l8e6:3 :f1l7u
setup@ressouhr cIe/:O/ /qgureeu/emdo dbuelfeosr/eT eplreomfeitlreyBSeefnodr.esCyhsa.nmgjes":
JavaScrip8t 0e3r:r1o8r
setup:@ rreessoouurrccee::////ggrree//mmoodduulleess//TAeslyenmceSthruytSdeonwdn..ssyyss..mmjjss:,2 4l1i:n3e0 
setupTe7l2e7m:e tErryr/otrh:i sP.h_adseel a"yperdoIfniilteT-absekf<o@rree-scohuarncgee:"/ /igs finisrhee/dm, it is otdouol elsa/te to registeTr coemlpelmeettiroynC ocnotnrdoiltlieornP a"rOeSn.tF.islyes:. mfjlsu:s8h2 9I:/3O6 
observe@reqsouueruceed: /b/egfroer/em opdruolfeisl/eABseyfnocrSehCuhtadnogwen".
sJayvsa.Smcjrsi:p5t7 6e:r1r6o

rWARNING: Erro:r :r ePshoausrec e":p/r/ogfriel/em-obdeufloerse/-AcshyanncgSeh"u tidso wfni.nsiyssh.emdj,s ,i tl is tionoe  l7a2t7: Error: Phase "profeile-be ftoor er-ecghiasntgeer"  ciosm pflientiisohne dc,o nidti tiiso nt o"oOS.File: flush I/O qu euleadt e to registber ceofmoprlee tpiroonf icloendBietfioorne Change"
WARNING": OaSd.dFBilloec:k efrl@ursehs oIu/rOc eq:u/e/ugerde /bmeofdourlee sp/rAosfyinlceSBheuftodroewCnh.asnygse."m
JavaScript errjors:: 7r2e7s:o1u5r
addBlocckee:r/@/rgerseo/umrocdeu:l/e/sg/rAes/ymnocdSuhluetsd/oAwsny.nscySsh.umtjdso,w nl.isnyes .7m2j7s:: 5E2r3r:o2r6:
addBlocker@res ourPchea:s/e/ g"rper/omfoidluel-ebse/fAosryen-ccShhauntgdeo"w ni.ss yfsi.mjs:458:15
setupShutdown@resourcen:i/s/hgerde,/ miotd uilse st/ooos flialtee/ otsof irleeg_iasstyenrc _cforont.jsm:m1p5l4e8t:i2o8n
@resourc ec:o/n/dgirtei/omno d"uOlSe.sF/iolsef:i lfel/uosshf iIl/eO_ aqsuyenuce_df rboenfto.rjes mp:r1o5f6i8l:e1B6e
@resourcef://ogrreeC/hmaondguel"e
JavaScripst/ oesrfriolre:. jrsems:o1u2r:c3e0:
@resource/:///ggrree//mmoodduulleess//ATseylnecmSehturtydSotwonr.asgyes..smyjss.,m jlsi:n1e0 :72287
_checkPen:di nEgrPrionrg:s @Prhesourcaes:e/ /"gprreo/fmioldeu-lbeesf/oTreel-ecmheatnrgyeS"e nids. sfyisn.imsjhse:d8,6 3i:t1 7i
setup@ress otuoroc el:a/t/eg rteo/ mroedguislteesr/ TceolmepmeltertyiSoenn dc.osnydsi.tmijosn: 8"0O3S:.1F8i
setup@relsouer:c ef:l/u/sghr eI//mOo dquuleeuse/dT beelfeomreet rpyrSoefnidl.esByesf.omrjesC:h2a4n1g:e3"0

setupTelemetry/this._delayedInitTask<@resource://gre/modules/TelemetryControllerParent.sys.mjs:829:36
observe@resource://gre/modules/AsyncShutdown.sys.mjs:576:16
```

With Logger set to `FINE`, the geckodriver logs are automatically turned off:

```java
new SeleniumLogger().setLevel(Level.FINE);
WebDriver driver = new ChromeDriver();
driver.quit();
```

```text
2023-03-15 13:06:28 FINE - Executing: newSession [null, newSession {capabilities=[Capabilities {acceptInsecureCerts: true, browserName: firefox, moz:debuggerAddress: true, moz:firefoxOptions: {}}], desiredCapabilities=Capabilities {acceptInsecureCerts: true, browserName: firefox, moz:debuggerAddress: true, moz:firefoxOptions: {}}}] 
2023-03-15 13:06:31 FINE - Executed: (Response: SessionID: 55edc384-3f79-40d8-9941-28bc8c1fbf7f, Status: 0, Value: {acceptInsecureCerts=true, browserName=firefox, browserVersion=110.0.1, moz:accessibilityChecks=false, moz:buildID=20230227191043, moz:debuggerAddress=127.0.0.1:7662, moz:geckodriverVersion=0.32.2, moz:headless=false, moz:platformVersion=20.6.0, moz:processID=10280, moz:profile=/var/folders/fx/nlx77ccs72g808pr1994_xtw0000gn/T/rust_mozprofilekIsm4x, moz:shutdownTimeout=60000, moz:useNonSpecCompliantPointerOrigin=false, moz:webdriverClick=true, moz:windowless=false, pageLoadStrategy=normal, platformName=mac, proxy=Proxy(), setWindowRect=true, strictFileInteractability=false, timeouts={implicit=0, pageLoad=300000, script=30000}, unhandledPromptBehavior=dismiss and notify}) 
2023-03-15 13:06:31 FINE - Executing: quit [55edc384-3f79-40d8-9941-28bc8c1fbf7f, quit {}] 
2023-03-15 13:06:32 FINE - Executed: (Response: SessionID: 55edc384-3f79-40d8-9941-28bc8c1fbf7f, Status: 0, Value: null) 
```

## Advanced Features

### Logger classes

As Selenium adds logging information to more classes, this project will be updated to include them.
If you want to change output from different classes, you can add or remove them.

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
