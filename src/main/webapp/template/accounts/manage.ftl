<#assign page_title="Account Manager">
<#include "/template/layouts/global_header.ftl">

<a href="/spendhawk/accounts?user.id=${user.id}"
   xmlns="http://www.w3.org/1999/html">Back to Accounts</a>

<p>
${message!""}
</p>

<h2>Add new account:</h2>
<form method="POST">
    Account Name: <input type="text" name="account.name" length="50"/>
    <br/>
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="store"/>
</form>

<h2>Delete account:</h2>
<p>
    DANGER: This will delete all transactions and expenses added to the account,
    and <span style="font-weight: bold; color: red;">CANNOT BE UNDONE</span>!
</p>
<form method="POST">
    Account Name:
    <select id="acctSelect" name="account.id">
        <#list user.accounts as account>
            <option value="${account.id}">${account.name}</option>
        </#list>
    </select><br/>
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="delete" onclick="confirm(getConfirmMessage())"/>
</form>

<script>
    function getConfirmMessage() {
        var e = document.getElementById('acctSelect');
        var acctId = e.options[e.selectedIndex].value;
        var acctName = e.options[e.selectedIndex].innerHTML;
        return 'Are you sure you want to delete the account \"' +
            acctName + ' with ID = ' + acctId + '\"?\n\nTHIS CANNOT BE UNDONE!';
    }
</script>

<#include "/template/layouts/global_footer.ftl">
