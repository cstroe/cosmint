<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->
<#-- @ftlvariable name="nextMonth" type="java.lang.String" -->
<#-- @ftlvariable name="previousMonth" type="java.lang.String" -->
<#-- @ftlvariable name="account" type="com.github.cstroe.spendhawk.entity.Account" -->

<#assign page_title="${account.name}">

<#include "/template/layouts/global_header.ftl"/>

<div class="accountnav">
    <a class="navlink" href="${fw.servlet("com.github.cstroe.spendhawk.web.user.UserSummaryServlet", "user.id", account.user.id)}">
        <img class="navbutton" src="/spendhawk/images/previous.svg"/>Accounts
    </a>
    <a class="navlink" href="${fw.servlet("com.github.cstroe.spendhawk.web.AddTransactionServlet", "user.id", account.user.id, "account.id", account.id)}">
        <img class="navbutton" src="/spendhawk/images/add.svg"/>Add Transaction
    </a>
    <a class="navlink" href="/spendhawk/transactions/upload?id=${account.id}">
        <img class="navbutton" src="/spendhawk/images/upload.svg">Upload Transactions
    </a>
    <a class="navlink" href="/spendhawk/category/bulk?account.id=${account.id}">
        <img class="navbutton" src="/spendhawk/images/add.svg">Bulk Categorization
    </a>
</div>

${account.name}<br/>
Balance: ${account.balance}<br/>

<div class="accountnav">
    <form style="margin: 0px;" method="get" action="/spendhawk/transaction/search">
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=${previousMonth}">
        <img class="navbutton" src="/spendhawk/images/back.svg"/>Previous Month
    </a>
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=currentMonth">
        <img class="navbutton" src="/spendhawk/images/calendar.svg"/>Current Month
    </a>
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=${nextMonth}">
        <img class="navbutton" src="/spendhawk/images/forward.svg"/>Next Month
    </a>
    <img class="navbutton" src="/spendhawk/images/search.svg"/>
    <input type="text" name="q"/>
    <input type="hidden" name="account.id" value="${account.id}"/>
    <input type="submit" name="action" value="search"/>
    </form>
</div>

<#include "/template/accounts/transactions_table.ftl"/>
<#include "/template/layouts/global_footer.ftl"/>