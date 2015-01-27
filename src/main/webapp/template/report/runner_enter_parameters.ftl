<#assign page_title="SpendHawk Reports">

<#include "/template/layouts/global_header.ftl"/>

<h2>Report Parameters</h2>
Report: ${report.name}<br/>

<form method="post">
    ${generate.form}
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="hidden" name="report.name" value="${report.name}"/>
    <input type="submit" name="action" value="Run Report"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>