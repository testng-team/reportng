<!doctype html>
<html lang="${messages.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${meta.reportTitle} - ${result.testContext.name}</title>

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
<h1>${result.testContext.name}</h1>

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

<p>${messages.getString("testDuration")}
    : ${utils.formatDuration(utils.getDuration(result.testContext))}s</p>

<#assign id=0>
<#if (failedConfigurations?size > 0)>
    <table class="resultsTable configTable">
        <tr>
            <th class="header failedConfig"
                colspan="3">${messages.getString("failedConfiguration")}</th>
        </tr>
        <#assign id=0>
        <#list failedConfigurations as testClass, classResults>
            <tr class="group">
                <td colspan="3">${testClass.name}</td>
            </tr>
            <#include "class-results.ftl">
        </#list>

        <#if (skippedConfigurations?size > 0)>
            <tr>
                <td colspan="3">&nbsp;</td>
            </tr>
            <tr>
                <td colspan="3"
                    class="header skippedConfig">${messages.getString("skippedConfiguration")}</td>
            </tr>
            <#assign id=0>
            <#list skippedConfigurations as testClass, classResults>
                <tr class="group">
                    <td colspan="3">${testClass.name}</td>
                </tr>
                <#include "class-results.ftl">
            </#list>
        </#if>
    </table>
</#if>

<#if (failedTests?size > 0)>
    <table class="resultsTable">
        <tr>
            <th colspan="3" class="header failed">${messages.getString("failedTests")}</th>
        </tr>
        <#list failedTests as testClass, classResults>
            <tr>
                <td class="group" colspan="3">${testClass.name}</td>
            </tr>
            <#include "class-results.ftl">
        </#list>
    </table>
</#if>

<#if (skippedTests?size > 0)>
    <table class="resultsTable" width="100%">
        <tr>
            <th colspan="3" class="header skipped">${messages.getString("skippedTests")}</th>
        </tr>
        <#list skippedTests as testClass, classResults>
            <tr>
                <td class="group" colspan="3">${testClass.name}</td>
            </tr>
            <#include "class-results.ftl">
        </#list>
    </table>
</#if>

<#if (passedTests?size > 0)>
    <table class="resultsTable" width="100%">
        <tr>
            <th colspan="3" class="header passed">${messages.getString("passedTests")}</th>
        </tr>
        <#list passedTests as testClass, classResults>
            <tr>
                <td class="group" colspan="3">${testClass.name}</td>
            </tr>
            <#include "class-results.ftl">
        </#list>
    </table>
</#if>

<script type="text/javascript" src="reportng.js"></script>
</body>
