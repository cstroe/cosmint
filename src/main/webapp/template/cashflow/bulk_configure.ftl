<#-- @ftlvariable name="cashflows" type="com.github.cstroe.spendhawk.entity.CashFlow[]" -->
<#-- @ftlvariable name="accountsAll" type="com.github.cstroe.spendhawk.entity.Account[]" -->
<#-- @ftlvariable name="accountsToReplace" type="com.github.cstroe.spendhawk.entity.Account[]" -->
<#-- @ftlvariable name="fromAccount" type="com.github.cstroe.spendhawk.entity.Account" -->
<#-- @ftlvariable name="query" type="java.lang.String" -->
<#assign page_title="${fromAccount.name}">

<#include "/template/layouts/global_header.ftl"/>

<h2>Bulk Categorizing Tool</h2>

Account: ${fromAccount.name}<br/>

<form method="post">
    Search string: ${query} <br/>
    Account to replace:
    <select name="accountToReplaceId">
        <#list accountsToReplace as account>
            <option value="${account.id}">${account.name}</option>
        </#list>
    </select><br/>

    Replacement account:
    <select name="replacementAccountId">
    <#list accountsAll as account>
        <option value="${account.id}">${account.name}</option>
    </#list>
    </select><br/>

    <br/>
    <#include "/template/accounts/transactions_table_with_checkbox.ftl"/>

    <input type="hidden" name="query" value="${query!""}"/>
    <input type="hidden" name="fromAccountId" value="${fromAccount.id}"/>
    <input type="submit" name="action" value="Preview"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>
