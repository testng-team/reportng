<!doctype html>
<html lang="${messages.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${meta.reportTitle} - ${messages.getString("suites")}</title>

    <link href="reportng.css" rel="stylesheet" type="text/css"/>
    <#if meta.stylesheetPath??>
        <link href="custom.css" rel="stylesheet" type="text/css"/>
    </#if>
</head>
<body>
<div id="meta">
${messages.getString("generatedBy")}
${messages.getString("atTime")} ${meta.reportTime} ${messages.getString("onDate")} ${meta.reportDate}
    <br/><span
    id="systemInfo">${meta.user}&nbsp;/&nbsp;${meta.javaInfo}&nbsp;/&nbsp;${meta.platform}</span>
</div>
<h1>${meta.reportTitle}</h1>

<p>
    <a href="index.html">${messages.getString("suites")}</a>
    <#if utils.allOutput??>
        &#183; <a href="output.html">${messages.getString("logOutput")}</a>
        <#if meta.coverageLink??>
            &#183; <a href="${meta.coverageLink}"
                      target="_top">${messages.getString("coverageReport")}</a>
        </#if>
    </#if>
</p>

<table id="suites" class="overviewTable">
    <#list suites as suite>
        <#assign totalTests = 0>
        <#assign totalPassed = 0>
        <#assign totalSkipped = 0>
        <#assign totalFailed = 0>
        <thead>
        <tr>
            <th colspan="6" class="header suite"
                onclick="toggleElement('tests-${suite?index}', 'table-row-group'); toggle('toggle-${suite?index}')">
                <div class="suiteLinks">
                    <#if utils.hasGroups(suite)>
                        <a href="suite${suite?index}_groups.html">${messages.getString("groups")}</a>
                    </#if>
                </div>
                <span id="toggle-${suite?index}" class="toggle">&#x25bc;</span>${suite.name}
            </th>
        </tr>
        </thead>
        <tbody id="tests-${suite?index}" class="tests">
        <tr class="columnHeadings">
            <td>&nbsp;</td>
            <th>${messages.getString("duration")}</th>
            <th>${messages.getString("passed")}</th>
            <th>${messages.getString("skipped")}</th>
            <th>${messages.getString("failed")}</th>
            <th>${messages.getString("passRate")}</th>
        </tr>
            <#assign suiteId=suite?index>
            <#list suite.results?values as result>
                <#assign notPassedTests = result.testContext.skippedTests.size() + result.testContext.failedTests.size()>
                <#assign total = result.testContext.passedTests.size() + notPassedTests>
                <#assign totalTests = totalTests + total>
                <#assign totalPassed = totalPassed + result.testContext.passedTests.size()>
                <#assign totalSkipped = totalSkipped + result.testContext.skippedTests.size()>
                <#assign totalFailed = totalFailed + result.testContext.failedTests.size()>
                <#assign failuresExist = (result.testContext.failedTests.size() > 0 || result.testContext.failedConfigurations.size() > 0)>

                <#if ((onlyReportFailures && failuresExist) || (!onlyReportFailures))>
                    <tr class="test">
                        <td class="test">
                            <#if (result.testContext.failedTests.size() > 0)>
                                <span style="float: none" class="failureIndicator"
                                      title="${messages.getString("failed.tooltip")}">&#x2718;</span>
                            <#elseif (result.testContext.skippedTests.size() > 0)>
                                <span style="float: none" class="skipIndicator"
                                      title="${messages.getString("skipped.tooltip")}">&#x2714;</span>
                            <#else>
                                <span style="float: none" class="successIndicator"
                                      title="${messages.getString("passed.tooltip")}">&#x2714;</span>
                            </#if>
                            <a href="suite${suiteId}_test${result?index}_results.html">${result.testContext.name}</a>
                        </td>

                        <td class="duration">
                            ${utils.formatDuration(utils.getDuration(result.testContext))}s
                        </td>

                        <#if (result.testContext.passedTests.size() > 0)>
                        <td class="passed number">${result.testContext.passedTests.size()}</td>
                        <#else>
                        <td class="zero number">0</td>
                        </#if>

                        <#if (result.testContext.skippedTests.size() > 0)>
                        <td class="skipped number">${result.testContext.skippedTests.size()}</td>
                        <#else>
                        <td class="zero number">0</td>
                        </#if>

                        <#if (result.testContext.failedTests.size() > 0)>
                        <td class="failed number">${result.testContext.failedTests.size()}</td>
                        <#else>
                        <td class="zero number">0</td>
                        </#if>

                        <td class="passRate">
                            <#if (total > 0)>
                                <#assign passes = total - notPassedTests>
                                ${utils.formatPercentage(passes, total)}
                            <#else>
                                ${messages.getString("notApplicable")}
                            </#if>
                        </td>
                    </tr>
                </#if>
            </#list>

        <tr class="suite">
            <td colspan="2" class="totalLabel">${messages.getString("total")}</td>

                <#if (totalPassed > 0)>
                    <td class="passed number">${totalPassed}</td>
                <#else>
                    <td class="zero number">0</td>
                </#if>

                <#if (totalSkipped > 0)>
                    <td class="skipped number">${totalSkipped}</td>
                <#else>
                    <td class="zero number">0</td>
                </#if>

                <#if (totalFailed > 0)>
                    <td class="failed number">${totalFailed}</td>
                <#else>
                    <td class="zero number">0</td>
                </#if>

            <td class="passRate suite">
                    <#if (totalTests > 0)>
                        <#assign totalPasses = totalTests - totalSkipped - totalFailed>
                        ${utils.formatPercentage(totalPasses, totalTests)}
                    <#else>
                        ${messages.getString("notApplicable")}
                    </#if>
            </td>
        </tr>
        </tbody>
    </#list>
</table>

<script type="text/javascript" src="reportng.js"></script>
</body>
</html>
