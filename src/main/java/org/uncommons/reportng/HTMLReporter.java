package org.uncommons.reportng;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.testng.IClass;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.xml.XmlSuite;

public class HTMLReporter extends AbstractReporter {

    private static final String ONLY_FAILURES_PROPERTY = "org.uncommons.reportng.failures-only";

    private static final String TEMPLATES_PATH = "org/uncommons/reportng/templates/html/";
    private static final String HTML_EXTENSION = ".html";
    private static final String FTL_EXTENSION = ".ftl";

    private static final String INDEX_FILE = "index";
    private static final String GROUPS_FILE = "groups";
    private static final String RESULTS_FILE = "results";
    private static final String OUTPUT_FILE = "output";
    private static final String CUSTOM_STYLE_FILE = "custom.css";

    private static final String SUITE_KEY = "suite";
    private static final String SUITES_KEY = "suites";
    private static final String GROUPS_KEY = "groups";
    private static final String RESULT_KEY = "result";
    private static final String FAILED_CONFIG_KEY = "failedConfigurations";
    private static final String SKIPPED_CONFIG_KEY = "skippedConfigurations";
    private static final String FAILED_TESTS_KEY = "failedTests";
    private static final String SKIPPED_TESTS_KEY = "skippedTests";
    private static final String PASSED_TESTS_KEY = "passedTests";
    private static final String ONLY_FAILURES_KEY = "onlyReportFailures";

    private static final String REPORT_DIRECTORY = "html";

    private static final Comparator<ITestNGMethod> METHOD_COMPARATOR = Comparator
        .comparing((ITestNGMethod method) -> method.getRealClass().getName())
        .thenComparing(ITestNGMethod::getMethodName);

    private static final Comparator<ITestResult> RESULT_COMPARATOR =
        Comparator.comparing(ITestResult::getName);

    private static final Comparator<IClass> CLASS_COMPARATOR =
        Comparator.comparing(IClass::getName);


    public HTMLReporter() {
        super(TEMPLATES_PATH);
    }

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
        String outputDirectoryName) {
        removeEmptyDirectories(new File(outputDirectoryName));

        boolean onlyFailures = System.getProperty(ONLY_FAILURES_PROPERTY, "false").equals("true");

        File outputDirectory = new File(outputDirectoryName, REPORT_DIRECTORY);
        outputDirectory.mkdirs();

        try {
            createSuiteList(suites, outputDirectory, onlyFailures);
            createGroups(suites, outputDirectory);
            createResults(suites, outputDirectory, onlyFailures);
            createLog(outputDirectory, onlyFailures);
            copyResources(outputDirectory);
        } catch (Exception ex) {
            throw new ReportNGException("Failed generating HTML report", ex);
        }
    }

    /**
     * Create the navigation frame.
     *
     * @param outputDirectory The target directory for the generated file(s).
     */
    private void createSuiteList(List<ISuite> suites,
        File outputDirectory,
        boolean onlyFailures) throws Exception {
        Map<String, Object> context = createContext();
        context.put(SUITES_KEY, suites);
        context.put(ONLY_FAILURES_KEY, onlyFailures);
        generateFile(new File(outputDirectory, INDEX_FILE + HTML_EXTENSION),
            INDEX_FILE + FTL_EXTENSION,
            context);
    }

    /**
     * Generate a results file for each test in each suite.
     *
     * @param outputDirectory The target directory for the generated file(s).
     */
    private void createResults(List<ISuite> suites, File outputDirectory, boolean onlyShowFailures)
        throws Exception {
        int i = 0;
        for (ISuite suite : suites) {
            int j = 0;
            for (ISuiteResult result : suite.getResults().values()) {
                boolean failuresExist = result.getTestContext().getFailedTests().size() > 0
                    || result.getTestContext().getFailedConfigurations().size() > 0;

                if (!onlyShowFailures || failuresExist) {
                    Map<String, Object> context = createContext();
                    context.put(RESULT_KEY, result);
                    context.put(FAILED_CONFIG_KEY,
                        sortByTestClass(result.getTestContext().getFailedConfigurations()));
                    context.put(SKIPPED_CONFIG_KEY,
                        sortByTestClass(result.getTestContext().getSkippedConfigurations()));
                    context.put(FAILED_TESTS_KEY,
                        sortByTestClass(result.getTestContext().getFailedTests()));
                    context.put(SKIPPED_TESTS_KEY,
                        sortByTestClass(result.getTestContext().getSkippedTests()));
                    context.put(PASSED_TESTS_KEY,
                        sortByTestClass(result.getTestContext().getPassedTests()));
                    String fileName = String
                        .format("suite%d_test%d_%s", i, j, RESULTS_FILE + HTML_EXTENSION);
                    generateFile(new File(outputDirectory, fileName),
                        RESULTS_FILE + FTL_EXTENSION,
                        context);
                }
                ++j;
            }
            ++i;
        }
    }

    private SortedMap<IClass, List<ITestResult>> sortByTestClass(IResultMap results) {
        SortedMap<IClass, List<ITestResult>> sortedResults = new TreeMap<>(CLASS_COMPARATOR);
        for (ITestResult result : results.getAllResults()) {
            List<ITestResult> resultsForClass = sortedResults
                .computeIfAbsent(result.getTestClass(), k -> new ArrayList<>());
            int index = Collections.binarySearch(resultsForClass, result, RESULT_COMPARATOR);
            if (index < 0) {
                index = Math.abs(index + 1);
            }
            resultsForClass.add(index, result);
        }
        return sortedResults;
    }

    private void createGroups(List<ISuite> suites, File outputDirectory) throws Exception {
        int index = 0;
        for (ISuite suite : suites) {
            SortedMap<String, SortedSet<ITestNGMethod>> groups = sortGroups(
                suite.getMethodsByGroups());
            if (!groups.isEmpty()) {
                Map<String, Object> context = createContext();
                context.put(SUITE_KEY, suite);
                context.put(GROUPS_KEY, groups);
                String fileName = String.format("suite%d_%s", index, GROUPS_FILE + HTML_EXTENSION);
                generateFile(new File(outputDirectory, fileName), GROUPS_FILE + FTL_EXTENSION,
                    context);
            }
            ++index;
        }
    }

    private SortedMap<String, SortedSet<ITestNGMethod>> sortGroups(
        Map<String, Collection<ITestNGMethod>> groups) {
        SortedMap<String, SortedSet<ITestNGMethod>> sortedGroups = new TreeMap<>();
        for (Map.Entry<String, Collection<ITestNGMethod>> entry : groups.entrySet()) {
            SortedSet<ITestNGMethod> methods = new TreeSet<>(METHOD_COMPARATOR);
            methods.addAll(entry.getValue());
            sortedGroups.put(entry.getKey(), methods);
        }
        return sortedGroups;
    }

    private void createLog(File outputDirectory, boolean onlyFailures) throws Exception {
        if (!Reporter.getOutput().isEmpty()) {
            Map<String, Object> context = createContext();
            context.put(ONLY_FAILURES_KEY, onlyFailures);
            generateFile(new File(outputDirectory, OUTPUT_FILE + HTML_EXTENSION),
                OUTPUT_FILE + FTL_EXTENSION,
                context);
        }
    }

    private void copyResources(File outputDirectory) throws IOException {
        copyClasspathResource(outputDirectory, "reportng.css", "reportng.css");
        copyClasspathResource(outputDirectory, "reportng.js", "reportng.js");

        // If there is a custom stylesheet, copy that.
        File customStylesheet = META.getStylesheetPath();

        if (customStylesheet != null) {
            if (customStylesheet.exists()) {
                copyFile(outputDirectory, customStylesheet, CUSTOM_STYLE_FILE);
            } else {
                // If not found, try to read the file as a resource on the classpath
                // useful when reportng is called by a jarred up library
                InputStream stream = ClassLoader.getSystemClassLoader()
                    .getResourceAsStream(customStylesheet.getPath());
                if (stream != null) {
                    copyStream(outputDirectory, stream, CUSTOM_STYLE_FILE);
                }
            }
        }
    }
}
