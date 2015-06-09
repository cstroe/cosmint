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

<form method="post">
<p>
    Description: <input type="text" name="description" value="${transaction.description}"/>
    <input type="submit" name="update.description" value="Update"/>
</p>

<input type="hidden" name="transactionId" value="${transaction.id}"/>
<input type="hidden" name="fromAccountId" value="${fromAccountId}"/>
</form>

<p>
    Note: ${transaction.notes}
</p>

<h2>CashFlows:</h2>
<form method="post">
<table border='1'>
    <tr>
        <th>Account</th>
        <th>Amount</th>
    </tr>

<#list transaction.cashFlows as cashFlow>
    <tr>
        <td>
            <select name="toAccountId[]">
                <#list cashFlow.account.user.accounts as acct>
                    <#if acct.name == cashFlow.account.name>
                        <option selected="" value="${acct.id}">${acct.name}</option>
                    <#else>
                        <option value="${acct.id}">${acct.name}</option>
                    </#if>
                </#list>
            </select>
            <input type="hidden" name="cfid[]" value="${cashFlow.id}"/>
        </td>
        <td>
            <input type="text" name="cfamount[]" value="${cashFlow.amount}"/>
        </td>

    </tr>
</#list>
</table>

    <input type="submit" name="update.cashflows" value="Update CashFlows"/>
    <input type="hidden" name="fromAccountId" value="${fromAccountId}"/>
    <input type="hidden" name="transactionId" value="${transaction.id}"/>
</form>
<#include "/template/layouts/global_footer.ftl">