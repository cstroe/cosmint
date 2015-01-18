<#assign page_title="Accounts">

<#include "/template/layouts/global_header.ftl">

<a href="${fw.url('/accounts/manage', "user.id", user.id)}">Manage Accounts</a>

<h2>Accounts in database:</h2>
<table border='1'>
    <tr>
        <th>Account Name</th>
        <th>Account Balance</th>
    </tr>

<#list accounts as account>
    <tr>
        <td>
            <a href="${fw.url('/account', 'id', account.id, 'relDate', 'currentMonth')}">
                ${account.name}
            </a>
        </td>
        <td>${account.balance}</td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">