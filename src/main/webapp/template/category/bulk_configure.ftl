<#assign page_title="${account.name}">

<#include "/template/layouts/global_header.ftl"/>

<h2>Bulk Categorizing Tool</h2>

Account: ${account.name}<br/>

<form method="post">
    Search string:<input type="text" name="q"/ value="${query!""}"><br/>
    Category to apply:
    <select name="category.id">
        <#list categories as category>
            <option value="${category.id}">${category.name}</option>
        </#list>
    </select><br/>
    Merchant: <input type="text" name="expense.merchant"/><br/>
    <input type="checkbox" name="duplicate_check" value="yes" checked/> Do not duplicate category.<br/>
    <input type="hidden" name="account.id" value="${account.id}"/>
    <input type="submit" name="action" value="Preview"/>
</form>

<#include "/template/layouts/global_footer.ftl"/>