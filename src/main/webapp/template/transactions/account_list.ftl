<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->
<select name="cfaccount[]">
    <option value=""></option>
    <#list user.accounts as userAccount>
        <option value="${userAccount.id}">${userAccount.name}</option>
    </#list>
</select>