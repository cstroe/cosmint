<#-- @ftlvariable name="accounts" type="com.github.cstroe.spendhawk.entity.Account[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#--noinspection FtlReferencesInspection-->
<a href="${fw.url('/accounts/manage', "user.id", user.id)}">Manage Accounts</a>

<link rel="stylesheet" type="text/css" media="all" href="/spendhawk/css/chart_of_accounts_table.css" />

<h2>Chart of Accounts:</h2>
<table id="chart_of_accounts">
    <tr>
        <th>Name</th>
        <th>Balance</th>
    </tr>

<#list accounts as account>
    <tr>
        <td class="account_cell">
        <#--noinspection FtlReferencesInspection-->
            <a href="${fw.url('/account', 'id', account.id, 'relDate', 'currentMonth')}">
                <span style="margin-left: ${account.depth * 20}px">${account.name}</span>
            </a>
        </td>
        <td class="coa_balance">${account.balance?string["0.00"]}</td>
    </tr>
</#list>

</table>