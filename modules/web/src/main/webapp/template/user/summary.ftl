<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->
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
        <a href="${fw.servlet("com.github.cstroe.spendhawk.web.ExportServlet", "user.id", user.id)}">Export Account Data</a>
    </div>

    <div class="grid_12 clearfix">
        <#include "/template/user/summary/accounts.ftl">
    </div>

    <div class="grid_12 clearfix">
        <#include "/template/user/summary/reports.ftl">
    </div>
</div>

<#include "/template/layouts/global_footer.ftl">