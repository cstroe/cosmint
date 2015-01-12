<#assign page_title="Account Manager">
<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/accounts">Back to Accounts</a>

<p>
${message!""}
</p>

<h2>Add new account:</h2>
<form method="POST">
    <input name="accountName" length="50"/>
    <br/>
    <input type="submit" name="action" value="store"/>
</form>


<#include "/template/layouts/global_footer.ftl">
