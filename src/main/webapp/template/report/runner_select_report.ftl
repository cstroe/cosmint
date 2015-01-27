<#assign page_title="SpendHawk Reports">

<#include "/template/layouts/global_header.ftl"/>

<form method="post">
    Report to run:
    <select name="report.name">
    <#list reports as report>
        <option value="${report.name}">${report.name}</option>
    </#list>
    </select><br/>
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="Enter Parameters"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>