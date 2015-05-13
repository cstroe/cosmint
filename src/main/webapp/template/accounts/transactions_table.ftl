<#-- @ftlvariable name="cashflows" type="com.github.cstroe.spendhawk.entity.CashFlow[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<table>
    <tr>
        <th class="date_cell">Date</th>
        <th class="desc_cell">Description</th>
        <th class="amnt_cell">Amount</th>
        <th class="note_cell">Notes</th>
    </tr>

<#list cashflows as cashflow>
    <tr>
        <td class="date_cell">${cashflow.transaction.effectiveDate?date?iso("CST")}</td>
        <td class="desc_cell">
            <a href="${fw.servlet("com.github.cstroe.spendhawk.web.transaction.TransactionView", "id", cashflow.transaction.id)}">
            ${cashflow.transaction.description}
            </a>
        </td>
        <td class="amnt_cell">${cashflow.amount?string["0.00"]}</td>
        <td class="note_cell">${cashflow.transaction.notes!""}</td>
    </tr>
</#list>
    <tr>
        <td class="date_cell">&nbsp;</td>
        <td class="desc_cell">&nbsp;</td>
        <td class="amnt_cell">-</td>
        <td class="note_cell">&nbsp;</td>
    </tr>

</table>