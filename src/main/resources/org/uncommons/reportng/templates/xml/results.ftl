<#-- @ftlvariable name="messages" type="java.util.ResourceBundle" -->
<#-- @ftlvariable name="utils" type="org.uncommons.reportng.ReportNGUtils" -->
<#-- @ftlvariable name="results" type="org.uncommons.reportng.JUnitXMLReporter.TestClassResults" -->
<?xml version="1.0" encoding="UTF-8"?>
<#assign totalTests = results.passedTests?size + results.skippedTests?size + results.failedTests?size>
<testsuite name="${results.testClass.xmlTest.name}"
           tests="${totalTests}"
           failures="${results.failedTests?size}"
           errors="0"
           skipped="${results.skippedTests?size}"
           time="${utils.formatDuration(results.duration)}">
    <properties></properties>

    <#list results.failedTests as testResult>
        <#if testResult.testName??>
            <testcase
                name="${testResult.name}"
                time="${utils.formatDuration(testResult.startMillis, testResult.endMillis)}"
                classname="${results.testClass.name} (${testResult.testName})">
        <#else>
            <testcase
                name="${testResult.name}"
                time="${utils.formatDuration(testResult.startMillis, testResult.endMillis)}"
                classname="${results.testClass.name}">
        </#if>

        <#if testResult.throwable??>
            <#if testResult.throwable.message??>
                <#assign message=utils.escapeString(testResult.throwable.message)>
            <#else>
                <#assign message="(null)">
            </#if>
            <failure type="${testResult.throwable.class.name}" message="${message}">
                <![CDATA[${testResult.throwable.toString()}
                <#list testResult.throwable.stackTrace as element>${element.toString()}</#list>
                <#assign causes=utils.getCauses(testResult.throwable)>
                <#list causes as throwable>
                    ${messages.getString("causedBy")}: ${throwable.toString()}
                    <#list throwable.stackTrace as element>${element.toString()}</#list>
                </#list>]]>
            </failure>
        <#else>
            <#if (testResult.status == 3)>
                <#assign message="Skipped">
            <#else>
                <#assign message="Unknown">
            </#if>
            <failure type="Unknown" message="${message}"></failure>
        </#if>
    </testcase>
    </#list>

    <#list results.skippedTests as testResult>
        <#if testResult.testName??>
            <testcase
                name="${testResult.name}"
                time="0.0"
                classname="${results.testClass.name} (${testResult.testName})">
            </testcase>
        <#else>
            <testcase
                name="${testResult.name}"
                time="0.0"
                classname="${results.testClass.name}">
            </testcase>
        </#if>
    </#list>

    <#list results.passedTests as testResult>
        <#if testResult.testName??>
            <testcase
                name="${testResult.name}"
                time="${utils.formatDuration(testResult.startMillis, testResult.endMillis)}"
                classname="${results.testClass.name} (${testResult.testName})">
            </testcase>
        <#else>
            <testcase
                name="${testResult.name}"
                time="${utils.formatDuration(testResult.startMillis, testResult.endMillis)}"
                classname="${results.testClass.name}">
            </testcase>
        </#if>
    </#list>
</testsuite>
