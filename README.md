# ReportNG

An HTML/XML Reporting Plugin for TestNG.

[ReportNG](https://testng.org/reportng/) is a simple HTML reporting plug-in for the [TestNG](http://www.testng.org/) unit-testing framework. It is intended as a replacement for the default TestNG HTML report.

### Pre-requisites
1. TestNG `v6.14.3` or higher.
2. JDK 8.

### How to use ReportNG

Add ReportNG as a Maven dependency

```xml
<dependency>
        <groupId>org.testng</groupId>
        <artifactId>reportng</artifactId>
        <version>1.2.2</version>
        <scope>test</scope>
</dependency>
```

Now ReportNG reports will be generated for each test run. ReportNG wires-in the below mentioned mandatory listeners via [Service Providers Interface](https://docs.oracle.com/javase/tutorial/sound/SPI-intro.html), so no additional work needs to be done to start consuming them. 

1. `org.uncommons.reportng.HTMLReporter`
2. `org.uncommons.reportng.JUnitXMLReporter`

### Supported System Properties (JVM arguments)

The following optional system properties can be set in order to customise the report output:

1. `org.uncommons.reportng.coverage-report` - A relative or absolute URL that links to a test coverage report.

2. `org.uncommons.reportng.escape-output` - Used to turn off escaping for log output in the reports (not recommended). The default is for output to be escaped, since this prevents characters such as `<` and `&` from causing mark-up problems. If escaping is turned off, then log text is included as raw HTML/XML, which allows for the insertion of hyperlinks and other nasty hacks.

3. `org.uncommons.reportng.failures-only` - Defaults to `false`. If set to `true`, the generated report will not list successful tests.

4. `org.uncommons.reportng.frames` - Defaults to `true`. If set to `false`, generates the HTML report without using a frameset. No navigation page is generated and the overview page becomes the index page.

5. `org.uncommons.reportng.locale` -  Overrides the default locale for localised messages in generated reports. If not specified, the JVM default locale is used. If there are no translations available for the selected locale the default English messages are used instead. This property should be set to an ISO language code (e.g. "en" or "fr") or to an ISO language code and an ISO country code separated by an underscore (e.g. "en_US" or "fr_CA"). ReportNG 1.1 includes translations for the following languages: English, French and Portuguese. If you would like to contribute translations for other languages, please open an issue in the issue tracker and attach a translated version of the properties file.

6. `org.uncommons.reportng.show-expected-exceptions` - Set to `true` or `false` to specify whether the stack-traces of expected exceptions should be included in the output for passed test cases. The default is `false` because the presence of stack-traces for successful tests may be confusing.

7. `org.uncommons.reportng.stylesheet` - The path to a custom CSS file that over-rides some or all of the default styles used by the HTMLReporter. This allows the appearance of reports to be customised. See the default stylesheet for the classes and selectors that can be styled. For an example, see this version of the sample report, which uses the bundled hudsonesque.css file to customise the report's appearance.

8. `org.uncommons.reportng.title` - Used to override the report title.

9. `org.uncommons.reportng.xml-dialect` -  Controls the XML generated by `JUnitXMLReporter`. If set to `testng` (the default), then skipped tests appear as `skipped` in the XML. This is suitable for use with tools such as Hudson that understand the TestNG dialect of the XML format. Other tools, such as Ant's junitreport task, do not have a notion of skipped tests. For these tools the dialect can be set to `junit` and skipped tests will be marked as failures.
