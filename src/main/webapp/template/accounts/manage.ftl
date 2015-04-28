<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#assign page_title="Account Manager">
<#include "/template/layouts/global_header.ftl">

<#--noinspection FtlReferencesInspection-->
<a href="${fw.servlet("com.github.cstroe.spendhawk.web.user.UserSummaryServlet","user.id", user.id)}">Back to User Summary</a>

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

<h2>Nest Account:</h2>
<form method="POST">
    Account (the parent):
    <select name="parentAccount.id">
        <#list user.accounts as currentAccount>
            <option class="accountOption" value="${currentAccount.id}" selected>${currentAccount.name}</option>
        </#list>
    </select>
    <br/>

    Sub-Account (the child):
    <select name="subAccount.id">
        <option value="null"></option>
        <#list user.accounts as currentAccount>
            <option class="subAccountOption" value="${currentAccount.id}" selected>${currentAccount.name}</option>
        </#list>
    </select>

    <br/>
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="Nest Account"/>
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

<form method="POST">
    <input type="hidden" name="user.id" value="${user.id}"/>
    <input type="submit" name="action" value="Convert Categories to Accounts"/>
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
