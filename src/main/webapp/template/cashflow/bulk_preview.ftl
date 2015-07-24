<#-- @ftlvariable name="replacement" type="com.github.cstroe.spendhawk.entity.Account" -->
<#-- @ftlvariable name="toReplace" type="com.github.cstroe.spendhawk.entity.Account" -->
<#-- @ftlvariable name="fromAccountId" type="java.lang.Long" -->
<#assign page_title="Bulk Update Preview">

<#include "/template/layouts/global_header.ftl"/>

<h2>Bulk Categorizing Tool - Preview</h2>

Account: ${account.name}<br/>

<#include "/template/accounts/transactions_table.ftl"/>

<form method="post">
    <input type="hidden" name="fromAccountId" value="${fromAccountId}"/>
    <input type="hidden" name="query" value="${query}"/>
    <input type="hidden" name="accountToReplaceId" value="${toReplace.id}"/>
    <input type="hidden" name="replacementAccountId" value="${replacement.id}"/>
    <input type="submit" name="action" value="Apply categories"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>
