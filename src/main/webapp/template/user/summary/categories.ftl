<#-- @ftlvariable name="categories" type="com.github.cstroe.spendhawk.entity.Category[]" -->
<#-- @ftlvariable name="fw" type="com.github.cstroe.spendhawk.util.TemplateForwarder" -->

<#--noinspection FtlReferencesInspection-->
<a href="${fw.url('/category/manage', "user.id", user.id)}">Manage Categories</a>

<h2>Categories:</h2>
<table border='1'>
    <tr>
        <th>Name</th>
    </tr>

<#list categories as category>
    <tr>
        <td>
            <#--noinspection FtlReferencesInspection-->
            <a class="categoryLink" href="${fw.url('/category', 'id', category.id)}">
            ${category.name}
            </a>
        </td>
    </tr>
</#list>

</table>
