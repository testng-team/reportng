# ReportNG

An HTML/XML Reporting Plugin for TestNG.

ReportNG is a simple HTML reporting plug-in for the [TestNG](http://www.testng.org/) unit-testing framework. It is intended as a replacement for the default TestNG HTML report.

### Pre-requisites
1. TestNG `v6.14.3` or higher.
2. JDK 8.

### How to use ReportNG

Add ReportNG as a Maven dependency

```xml
<dependency>
        <groupId>org.testng</groupId>
        <artifactId>reportng</artifactId>
        <version>1.2.0-SNAPSHOT</version>
        <scope>test</scope>
</dependency>
```

Now ReportNG reports will be generated for each test run. ReportNG wires-in the below mentioned mandatory listeners via [Service Providers Interface](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html), so no additional work needs to be done to start consuming them. 

1. `org.uncommons.reportng.HTMLReporter`
2. `org.uncommons.reportng.JUnitXMLReporter`
