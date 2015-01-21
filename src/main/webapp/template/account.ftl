<#assign page_title="${account.name}">

<#include "/template/layouts/global_header.ftl">

<div class="accountnav">
    <a class="navlink" href="/spendhawk/accounts?user.id=${account.user.id}">
        <img class="navbutton" src="/spendhawk/images/previous.svg"/>Accounts
    </a>
    <a class="navlink" href="/spendhawk/transactions/add?id=${account.id}">
        <img class="navbutton" src="/spendhawk/images/add.svg"/>Add Transaction
    </a>
    <a class="navlink" href="/spendhawk/transactions/upload?id=${account.id}">
        <img class="navbutton" src="/spendhawk/images/upload.svg">Upload Transactions
    </a>
</div>

${account.name}<br/>
Balance: ${account.balance}<br/>

<div class="accountnav">
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=${previousMonth}">
        <img class="navbutton" src="/spendhawk/images/back.svg"/>Previous Month
    </a>
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=currentMonth">
        <img class="navbutton" src="/spendhawk/images/calendar.svg"/>Current Month
    </a>
    <a class="navlink" href="/spendhawk/account?id=${account.id}&relDate=${nextMonth}">
        <img class="navbutton" src="/spendhawk/images/forward.svg"/>Next Month
    </a>
</div>

<table border='1' style="margin-left: auto; margin-right: auto;">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Amount</th>
        <th>Notes</th>
        <th>Categories</th>
    </tr>

<#list transactions as transaction>
    <tr>
        <td>${transaction.effectiveDate?date?iso("CST")}</td>
        <td>
            <a href="/spendhawk/transaction?id=${transaction.id}">
                ${transaction.description}
            </a>
        </td>
        <td style="text-align: right;">${transaction.amount?string["0.00"]}</td>
        <td>${transaction.notes!""}</td>
        <td style="font-size: 75%;">
            <#list transaction.expenses as expense>
                <span style="color: white; background-color: RoyalBlue; border-radius: 5px; border: solid RoyalBlue 1px; padding: 1px;">${expense.category.name}</span> (${expense.amount})
            </#list>
        </td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">