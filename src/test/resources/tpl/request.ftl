<#ftl output_format="HTML">
<#-- @ftlvariable name="data" type="io.qameta.allure.attachment.http.HttpRequestAttachment" -->
<#-- Значения секретных заголовков и cookie заменяются на ***, чтобы токены не попадали в Allure-отчёт.
     Блок curl не выводится: в нём заголовки печатаются целиком. -->
<#assign secretNames = ["x-access-token", "authorization", "token", "cookie"]>
<div>
    <#if data.method??><pre><code>${data.method}</code></pre><#else>Unknown</#if>
    <#if data.url??><div>${data.url}</div><#else>Unknown</#if>
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
    <div><pre><code>${name}: <#if secretNames?seq_contains(name?lower_case)>***<#else>${value}</#if></code></pre></div>
    </#list>
</div>
</#if>
<#if (data.cookies)?has_content>
<h4>Cookies</h4>
<div>
    <#list data.cookies as name, value>
    <div><pre><code>${name}: <#if secretNames?seq_contains(name?lower_case)>***<#else>${value}</#if></code></pre></div>
    </#list>
</div>
</#if>
