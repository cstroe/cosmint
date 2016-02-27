<#-- @ftlvariable name="cashflows" type="com.github.cstroe.spendhawk.entity.CashFlow[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<link rel="stylesheet" type="text/css" media="all" href="/spendhawk/css/transactions_table.css" />

<table>
    <tr>
        <th class="date_cell">Date</th>
        <th class="desc_cell">Description</th>
        <th class="amnt_cell">Amount</th>
        <th class="note_cell">Notes</th>
        <th class="acct_cell">Account</th>
    </tr>

<#list cashflows as cashflow>
    <tr>
        <td class="date_cell">${cashflow.effectiveDate?date?iso("CST")}</td>
        <td class="desc_cell">
            <a href="${fw.servlet("com.github.cstroe.spendhawk.web.transaction.TransactionView",
                        "id", cashflow.transaction.id, "fromAccountId", cashflow.account.id)}">
            ${cashflow.description}
            </a>
        </td>
        <td class="amnt_cell">${cashflow.amount?string["0.00"]}</td>
        <td class="note_cell">${cashflow.transaction.notes!""}</td>
        <td class="acct_cell">
            <#list cashflow.transaction.cashFlows as otherCf>
                <#if otherCf.id != cashflow.id>
                    <span class="acct_span">${otherCf.account.name}</span>
                </#if>
            </#list>
        </td>
    </tr>
</#list>
    <tr>
        <td class="date_cell">&nbsp;</td>
        <td class="desc_cell">&nbsp;</td>
        <td class="amnt_cell">-</td>
        <td class="note_cell">&nbsp;</td>
        <td class="note_cell">&nbsp;</td>
    </tr>

</table>