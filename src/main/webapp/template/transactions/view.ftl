<#assign page_title="View Transaction">
<#include "/template/layouts/global_header.ftl">

<p>
    <#assign transactionDate = transaction.effectiveDate?date?string("MM-dd-yyyy")>
    <a href="/spendhawk/account?id=${transaction.account.id}&relDate=${transactionDate}">Back to Account</a>
</p>

<form method="post">
    <input type="hidden" name="id" value="${transaction.id}"/>
    Delete this transaction: <input type="submit" value="Delete" name="delete"/>
</form>

<p>
    <a href="/spendhawk/expense/manage?transaction.id=${transaction.id}">Add Expense</a>
</p>


<h2>Expenses:</h2>
<table border='1'>
    <tr>
        <th>Amount</th>
        <th>Category</th>
        <th>Merchant</th>
    </tr>

<#list expenses as expense>
    <tr>
        <td>${expense.amount}</td>
        <td>${expense.category.name}</td>
        <td>${expense.merchant!""}</td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">