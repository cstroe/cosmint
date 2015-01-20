<a href="${fw.url('/category/manage', "user.id", user.id)}">Manage Categories</a>

<h2>Categories:</h2>
<table border='1'>
    <tr>
        <th>Name</th>
    </tr>

<#list categories as category>
    <tr>
        <td>
            <a href="${fw.url('/category', 'id', category.id)}">
            ${category.name}
            </a>
        </td>
    </tr>
</#list>

</table>
