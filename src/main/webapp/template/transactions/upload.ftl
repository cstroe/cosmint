<#assign page_title="Upload Transactions">
<#include "/template/layouts/global_header.ftl">

<p>
    <a href="/spendhawk/account?id=${account.id}&relDate=currentMonth">Back to Account</a>
</p>

<p>
<#list messages as message>
    ${message}<br/>
</#list>
</p>

<hr/>

<form method="post" action="upload" enctype="multipart/form-data" >
    File:
    <input type="file" name="file" id="file" /> <br/>
    Format:
    <select name="format">
        <option value="chase" selected>Chase</option>
        <option value="capone">Capital One</option>
    </select><br/>

    Duplicate check: <input type="checkbox" name="duplicate_check" value="yes" checked/><br/>

    <input type="hidden" name="id" value="${account.id}"/>
    <input type="submit" value="Upload" name="upload" id="upload" />
</form>

<#include "/template/layouts/global_footer.ftl">