<#assign page_title="SpendHawk Reports">

<#include "/template/layouts/global_header.ftl"/>

<a href="/spendhawk/accounts?user.id=${user.id}">Back to User Summary</a>

<p>
    ${renderer.render()}
</p>

<#include "/template/layouts/global_footer.ftl"/>