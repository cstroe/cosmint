<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->
<#-- @ftlvariable name="message" type="java.lang.String" -->
<#-- @ftlvariable name="category" type="com.github.cstroe.spendhawk.entity.Category" -->
<#-- @ftlvariable name="userCategories" type="com.github.cstroe.spendhawk.entity.Category[]" -->

<#assign page_title="Category ${category.name}">

<#include "/template/layouts/global_header.ftl"/>
<p>
    <#--noinspection FtlReferencesInspection-->
    <a href="${fw.servlet('com.github.cstroe.spendhawk.web.user.UserSummaryServlet', 'user.id', user.id)}">Back to Summary page</a>
</p>

<p>
    ${message!""}
</p>

Category: ${category.name}<br/>
<#if category.parent??>
    Category Parent: <span id="categoryParent">${category.parent.name}</span>
<#else>
    Category Parent: None.
</#if>

<form method="post">
    <input type="hidden" name="category.id" value="${category.id}"/>
    <input type="submit" name="action" value="Delete"/>
</form>

<form method="post">
    Set the parent category:
    <select name="parentCategory.id">
    <#if category.parent??>
        <option value="null"></option>
    <#else>
        <option value="null" selected></option>
    </#if>
    <#list userCategories as currentCategory>
        <#if category.parent?? && category.parent.id == currentCategory.id>
            <option class="categoryOption" value="${currentCategory.id}" selected>${currentCategory.name}</option>
        <#else>
            <option class="categoryOption" value="${currentCategory.id}">${currentCategory.name}</option>
        </#if>
    </#list>

    <input type="hidden" name="category.id" value="${category.id}"/>
    <input type="submit" name="action" value="Set Parent Category"/>
</form>


<#include "/template/layouts/global_footer.ftl"/>