<#-- @ftlvariable name="accounts" type="com.github.cstroe.spendhawk.entity.Account[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#--noinspection FtlReferencesInspection-->
<a href="${fw.url('/accounts/manage', "user.id", user.id)}">Manage Accounts</a>

<h2>Accounts:</h2>
<table border='1'>
    <tr>
        <th>Name</th>
        <th>Balance</th>
    </tr>

<#list accounts as account>
    <tr>
        <td>
            <#--noinspection FtlReferencesInspection-->
            <a href="${fw.url('/account', 'id', account.id, 'relDate', 'currentMonth')}">
            ${account.name}
            </a>
        </td>
        <td>${account.balance}</td>
    </tr>
</#list>

</table>