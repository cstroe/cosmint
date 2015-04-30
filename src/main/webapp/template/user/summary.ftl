<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->

<#assign page_title="User Summary">

<#include "/template/layouts/global_header.ftl">

<div class="container_12">
    <div class="grid_12 clearfix">
        <p>
            User: ${user.name}
        </p>
    </div>

    <div class="grid_12 clearfix">
        <#include "/template/user/summary/accounts.ftl">
    </div>

    <div class="grid_12 clearfix">
        <#include "/template/user/summary/reports.ftl">
    </div>
</div>

<#include "/template/layouts/global_footer.ftl">