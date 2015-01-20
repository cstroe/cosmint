<#assign page_title="Add Expense">
<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/account?id=${transaction.account.id}&relDate=currentMonth">Back to Account Summary</a>

<table>
    <tr>
        <td>Transaction:</td>
        <td>${transaction.description}</td>
    </tr>
    <tr>
        <td>Date:</td>
        <td>${transaction.effectiveDate}</td>
    </tr>
    <tr>
        <td>Notes:</td>
        <td>${transaction.notes!""}</td>
    </tr>
</table>

<p>
${message!""}
</p>

<h2>Add new expense:</h2>
<form method="POST">
    Expense Amount: <input type="text" name="expense.amount" value="${transaction.amount}" length="50"/>
    <br/>
    Category: <select name="category.id">
        <#list categories as category>
            <option value="${category.id}">${category.name}</option>
        </#list>
    </select>
    <br/>
    Merchant: <input type="text" name="expense.merchant" length="100"/>
    <br/>
    <input type="hidden" name="transaction.id" value="${transaction.id}"/>
    <input type="submit" name="action" value="store"/>
</form>

<#include "/template/layouts/global_footer.ftl">