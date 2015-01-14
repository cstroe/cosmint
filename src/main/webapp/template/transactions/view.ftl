<#assign page_title="View Transaction">
<#include "/template/layouts/global_header.ftl">

<form method="post">
    <input type="hidden" name="id" value="${transaction.id}"/>
    Delete this transaction: <input type="submit" value="Delete" name="delete"/>
</form>

<form method="post">
    <input type="hidden" name="id" value="${transaction.id}"/>

</form>

<#include "/template/layouts/global_footer.ftl">