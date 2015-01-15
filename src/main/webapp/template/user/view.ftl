<#assign page_title="Users">

<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/users/manage">Manage Users</a>

<h2>Users in database:</h2>
<table border='1'>
    <tr>
        <th>User Name</th>
    </tr>

<#list users as user>
    <tr>
        <td>
            <a href="/spendhawk/accounts?id=${user.id}">
            ${user.name}
            </a>
        </td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">