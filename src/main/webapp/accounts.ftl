<html>

<head>
    <title>Account Manager</title>
</head>

<body>

<p>
    ${message!""}
</p>

<h2>Add new account:</h2>
<form method="POST">
    <input name="accountName" length="50"/>
    <br/>
    <input type="submit" name="action" value="store"/>
</form>

<h2>Accounts in database:</h2>
<table border='1'>
    <tr>
        <th>Account Name</th>
        <th>Account Balance</th>
    </tr>

<#list accounts as account>
    <tr>
        <td>${account.name}</td>
        <td>${account.balance}</td>
    </tr>
</#list>

</table>

</body>
</html>