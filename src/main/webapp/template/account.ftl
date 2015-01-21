<#assign page_title="${account.name}">

<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/accounts?user.id=${account.user.id}">Back to User Summary</a>

<h2>${account.name}</h2>
<p>
    <a href="/spendhawk/transactions/add?id=${account.id}">Add Transaction</a>
</p>
<p>
    <a href="/spendhawk/transactions/upload?id=${account.id}">Upload Transactions File</a>
</p>

<p>
    Balance: ${account.balance}
</p>

<a href="/spendhawk/account?id=${account.id}&relDate=currentMonth">Current Month</a><br/>
<a href="/spendhawk/account?id=${account.id}&relDate=${previousMonth}">Previous Month</a><br/>
<a href="/spendhawk/account?id=${account.id}&relDate=${nextMonth}">Next Month</a><br/>

<table border='1'>
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