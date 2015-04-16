<#-- @ftlvariable name="renderer" type="com.github.cstroe.spendhawk.report.ReportResultRenderer" -->
<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#assign page_title="SpendHawk Reports">

<#include "/template/layouts/global_header.ftl"/>

<a href="${fw.servlet("com.github.cstroe.spendhawk.web.user.UserSummaryServlet","user.id", user.id)}">Back to User Summary</a>

<p>
    ${renderer.render()}
</p>

<#include "/template/layouts/global_footer.ftl"/>