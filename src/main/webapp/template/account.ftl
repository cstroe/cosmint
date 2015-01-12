<#assign page_title="${account.name}">

<#include "/template/layouts/global_header.ftl">

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

<table border='1'>
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Amount</th>
        <th>Notes</th>
    </tr>

<#list transactions as transaction>
    <tr>
        <td>${transaction.effectiveDate}</td>
        <td>${transaction.description}</td>
        <td>${transaction.amount}</td>
        <td>${transaction.notes!""}</td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">