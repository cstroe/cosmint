<#-- @ftlvariable name="query" type="java.lang.String" -->
<#-- @ftlvariable name="account" type="com.github.cstroe.spendhawk.entity.Account" -->
<#assign page_title="Search Transactions">

<#include "/template/layouts/global_header.ftl"/>

${account.name}<br/>
Balance: ${account.balance}<br/>

<div class="accountnav">
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=currentMonth">
        <img class="navbutton" src="/spendhawk/images/previous.svg"/>Back to Account
    </a>
    <a class="navlink" href="/spendhawk/category/bulk?account.id=${account.id}&q=${query}">
        <img class="navbutton" src="/spendhawk/images/add.svg">Bulk Categorization
    </a>
</div>

<#include "/template/accounts/transactions_table.ftl"/>
<#include "/template/layouts/global_footer.ftl"/>