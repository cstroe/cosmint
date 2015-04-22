<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->

<#assign page_title="User Summary">

<#include "/template/layouts/global_header.ftl">

<p>
    User: ${user.name}
</p>

<#include "/template/user/summary/accounts.ftl">

<#include "/template/user/summary/categories.ftl">

<#include "/template/user/summary/reports.ftl">

<#include "/template/layouts/global_footer.ftl">