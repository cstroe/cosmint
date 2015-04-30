<table border='1' style="margin-left: auto; margin-right: auto;">
    <tr>
        <th>Date</th>
        <th>Description</th>
        <th>Amount</th>
        <th>Categories</th>
        <th>Notes</th>
    </tr>

<#list transactions as transaction>
    <tr>
        <td>${transaction.effectiveDate?date?iso("CST")}</td>
        <td>
            <a href="/spendhawk/transaction?id=${transaction.id}">
            ${transaction.description}
            </a>
        </td>
        <td style="text-align: right;">${transaction.amount?string["0.00"]}</td>
        <td style="font-size: 75%;">
            <#list transaction.expenses as expense>
                <span style="color: white; background-color: RoyalBlue; border-radius: 5px; border: solid RoyalBlue 1px; padding: 1px;">${expense.category.name}</span> (${expense.amount})
            </#list>
        </td>
        <td>${transaction.notes!""}</td>
    </tr>
</#list>
    <tr>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
        <td style="text-align: right;">-</td>
        <td>&nbsp;</td>
        <td>&nbsp;</td>
    </tr>

</table>