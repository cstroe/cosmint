<#assign page_title="Category ${category.name}">

<#include "/template/layouts/global_header.ftl"/>

Category: ${category.name}<br/>

<form method="post">
    <input type="hidden" name="category.id" value="${category.id}"/>
    <input type="submit" name="action" value="Delete"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>