<#list classResults as testResult>
    <tr>
        <td class="method">
            <#assign testInstanceName = "">
            <#if testResult.testName??>
                <#assign testInstanceName = " ${testResult.testName}">
            </#if>
            <#if (testResult.method.description?? && testResult.method.description?length > 0)>
                <span class="description" title="${testResult.method.description}">
                    ${testResult.name}${testInstanceName}
                </span>
            <#else>
                ${testResult.name}${testInstanceName}
            </#if>
        </td>
        <td class="duration">
            ${utils.formatDuration(testResult.startMillis, testResult.endMillis)}s
        </td>
        <td class="result">
            <!-- Display the dependencies for skipped test methods -->
            <#if (testResult.status == 3)>
                <#if utils.hasDependentGroups(testResult)>
                    <i>${messages.getString("dependsOnGroups")}: </i>
                    <span class="dependency">
                        ${utils.getDependentGroups(testResult)}
                    </span>
                    <br/>
                </#if>

                <#if utils.hasDependentMethods(testResult)>
                    <i>${messages.getString("dependsOnMethods")}</i>
                    <span class="dependency">
                        ${utils.getDependentMethods(testResult)}
                    </span>
                </#if>

                <#if utils.hasSkipException(testResult)>
                    <i>${messages.getString("skipped.reason")}</i>
                    <span class="dependency">
                        ${utils.getSkipExceptionMessage(testResult)}
                    </span>
                </#if>
            </#if>

            <#if utils.hasArguments(testResult)>
                <i>${messages.getString("methodArguments")}: </i>
                <span class="arguments">
                    ${utils.getArguments(testResult)}
                </span>
                <br/>
            </#if>

            <!-- Show logger output for the test -->
            <#assign output = utils.getTestOutput(testResult)>
            <#if (output?size > 0)>
                <div class="testOutput">
                    <#list output as line>
                        <#if meta.shouldEscapeOutput??>
                            ${utils.escapeHTMLString(line)}<br/>
                        <#else>
                            ${line}
                        </#if>
                    </#list>
                </div>
            </#if>

            <#if (testResult.throwable?? && (testResult.status == 2 || meta.shouldShowExpectedExceptions()))>
                <a href="javascript:toggleElement('exception-${testResult?index}', 'block')"
                   title="${messages.getString("clickToExpandCollapse")}">
                    <b>${utils.escapeHTMLString(testResult.throwable?string)}</b>
                </a>
                <br/>
                <div class="stackTrace" id="exception-${testResult?index}">
                    <#list testResult.throwable.stackTrace as element>
                        ${utils.escapeHTMLString(element?string)}<br/>
                    </#list>
                    <#assign causes=utils.getCauses(testResult.throwable)>
                    <#list causes as throwable>
                        <b>${messages.getString("causedBy")}: </b>
                        <a href="javascript:toggleElement('exception-${throwable?index + 1}', 'block')"
                           title="Click to expand/collapse">
                            ${utils.escapeHTMLString(throwable.toString())}
                        </a>
                        <br/>
                        <div class="stackTrace" id="exception-${throwable?index + 1}">
                            <#list throwable.stackTrace as element>
                                ${utils.escapeHTMLString(element.toString())}<br/>
                            </#list>
                        </div>
                    </#list>
                </div>
            </#if>
        </td>
    </tr>
</#list>
