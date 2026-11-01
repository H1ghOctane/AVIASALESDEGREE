<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpResponseAttachment" -->
<div>
    <#if data.url??><div>${data.url}</div></#if>
    <#if data.responseCode??><h4>Status code</h4><div><pre><code>${data.responseCode?c}</code></pre></div></#if>
</div>
<#if data.body??>
<h4>Body</h4>
<div>
    <pre><code>${data.body}</code></pre>
</div>
</#if>
<#if (data.headers)?has_content>
<h4>Headers</h4>
<div>
    <#list data.headers as name, value>
    <div><pre><code>${name}: ${value}</code></pre></div>
    </#list>
</div>
</#if>
<#if (data.cookies)?has_content>
<h4>Cookies</h4>
<div>
    <#list data.cookies as name, value>
    <div><pre><code>${name}: ${value}</code></pre></div>
    </#list>
</div>
</#if>
