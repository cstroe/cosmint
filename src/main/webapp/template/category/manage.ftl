<#assign page_title="Manage Categories">
<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/accounts?user.id=${user.id}">Back to User Summary</a>

<p id="errorMessage" style="color: red;">
${message!""}
</p>

<h2>Add new category:</h2>
<form method="POST">
    Category Name: <input type="text" name="category.name" length="50"/>
    <br/>
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="store"/>
</form>

<#include "/template/layouts/global_footer.ftl">