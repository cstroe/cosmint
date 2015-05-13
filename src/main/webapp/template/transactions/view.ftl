<#-- @ftlvariable name="fromAccountId" type="java.lang.Long" -->
<#-- @ftlvariable name="transaction" type="com.github.cstroe.spendhawk.entity.Transaction" -->
<#assign page_title="View Transaction">
<#include "/template/layouts/global_header.ftl">

<p>
    <#assign transactionDate = transaction.effectiveDate?date?string("MM-dd-yyyy")>
    <a href="/spendhawk/account?id=${fromAccountId}&relDate=${transactionDate}">Back to Account</a>
</p>

<form method="post">
    <input type="hidden" name="id" value="${transaction.id}"/>
    <input type="hidden" name="fromAccountId" value="${fromAccountId}"/>
    Delete this transaction: <input type="submit" value="Delete" name="delete"/>
</form>

<p>
    <a href="/spendhawk/expense/manage?transaction.id=${transaction.id}">Add Expense</a>
</p>

<p>
    Note: ${transaction.notes}
</p>

<h2>CashFlows:</h2>
<table border='1'>
    <tr>
        <th>Account</th>
        <th>Amount</th>
    </tr>

<#list transaction.cashFlows as cashFlow>
    <tr>
        <td>${cashFlow.amount}</td>
        <td>${cashFlow.account.name}</td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">