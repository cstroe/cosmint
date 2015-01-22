<#assign page_title="Add Transaction">

<#include "/template/layouts/global_header.ftl">

<form method="post">
    Date (MM/dd/yyyy): <input type="text" name="date"/><br/>
    Amount: <input type="text" name="amount"/><br/>
    Description: <input type="text" name="description"/><br/>
    Notes: <input type="text" name="notes"/><br/>
    <input type="hidden" name="account_id" value="${account.id}"/>
    <input type="submit" value="Add Transaction"/>
</form>

<#include "/template/layouts/global_footer.ftl">