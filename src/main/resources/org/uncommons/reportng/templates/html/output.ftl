<!doctype html>
<html lang="${messages.locale.language}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>${meta.reportTitle} - ${messages.getString("logOutput")}</title>

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
<h1>${messages.getString("logOutput")}</h1>
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
<p>
${messages.getString("logOutput.description")}
</p>

<div id="log">
    <#list utils.allOutput as line>
        <#if meta.shouldEscapeOutput()>
            ${utils.escapeHTMLString(line)}<br/>
        <#else>
            ${line}
        </#if>
    </#list>
</div>

</body>
</html>
