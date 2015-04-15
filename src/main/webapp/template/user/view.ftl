<#-- @ftlvariable name="users" type="com.github.cstroe.spendhawk.entity.User[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#assign page_title="Users">

<#include "/template/layouts/global_header.ftl">

<a href="${fw.url('/users/manage')}">Manage Users</a>

<h2>Users in database:</h2>
<table border='1'>
    <tr>
        <th>User Name</th>
    </tr>

<#list users as user>
    <tr>
        <td>
            <a class="userLink"
               <#--noinspection FtlReferencesInspection-->
               href="${fw.servlet('com.github.cstroe.spendhawk.web.user.UserSummaryServlet', 'user.id', user.id)}">
            ${user.name}
            </a>
        </td>
    </tr>
</#list>

</table>

<#include "/template/layouts/global_footer.ftl">