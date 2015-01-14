<#assign page_title="Add User">

<#include "/template/layouts/global_header.ftl">

<p style="color: red;">
    ${message!""}
</p>

<form method="post">
    User name: <input type="text" name="user.name"/><br/>
    <input type="submit" name="action" value="Add User"/>
</form>

<#include "/template/layouts/global_footer.ftl">