<#assign page_title="View Transaction">
<#include "/template/layouts/global_header.ftl">

<form method="post">
    <input type="hidden" name="id" value="${transaction.id}"/>
    <input type="submit" value="Delete" name="delete"/>
</form>

<#include "/template/layouts/global_footer.ftl">