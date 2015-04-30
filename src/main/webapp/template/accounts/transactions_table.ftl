<#-- @ftlvariable name="cashflows" type="com.github.cstroe.spendhawk.entity.CashFlow[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<table border='1' style="margin-left: auto; margin-right: auto;">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Amount</th>
        <th>Notes</th>
    </tr>

<#list cashflows as cashflow>
    <tr>
        <td>${cashflow.transaction.effectiveDate?date?iso("CST")}</td>
        <td>
            <a href="${fw.servlet("com.github.cstroe.spendhawk.web.transaction.TransactionView", "id", cashflow.transaction.id)}">
            ${cashflow.transaction.description}
            </a>
        </td>
        <td>${cashflow.amount}</td>
        <td>${cashflow.transaction.notes!""}</td>
    </tr>
</#list>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td style="text-align: right;">-</td>
        <td>&nbsp;</td>
    </tr>

</table>