<#assign page_title="Bulk Update Preview">

<#include "/template/layouts/global_header.ftl"/>

<h2>Bulk Categorizing Tool - Preview</h2>

Account: ${account.name}<br/>

<#include "/template/accounts/transactions_table.ftl"/>

<form method="post">
    <input type="hidden" name="account.id" value="${account.id}"/>
    <input type="hidden" name="category.id" value="${category.id}"/>
    <input type="hidden" name="expense.merchant" value="${merchant}"/>
    <input type="hidden" name="q" value="${q}"/>
    <input type="hidden" name="duplicate_check" value="${duplicateCheck}"/>
    <input type="submit" name="action" value="Apply categories"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>