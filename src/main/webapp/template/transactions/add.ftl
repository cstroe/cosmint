<#-- @ftlvariable name="account" type="com.github.cstroe.spendhawk.entity.Account" -->
<#-- @ftlvariable name="user" type="com.github.cstroe.spendhawk.entity.User" -->

<#assign page_title="Add Transaction">

<#include "/template/layouts/global_header.ftl">

<div class="container_12">
    <div class="grid_12">
        <form method="post">
            <table style="padding: 10px;" ce>
                <tr>
                    <td style="padding: 5px; text-align: right;">Effective Date (MM/dd/yyyy):</td>
                    <td style="padding: 5px;"><input type="text" name="date"/></td>
                </tr>

                <tr>

                <tr>
                    <td style="padding: 5px; text-align: right;">Description:</td>
                    <td style="padding: 5px;"><input type="text" name="description"/></td>
                </tr>

                <tr>
                    <td style="padding: 5px; text-align: right;">Notes:</td>
                    <td style="padding: 5px;"><input type="text" name="notes"/></td>
                </tr>
            </table>

            <h2>Cashflows:</h2>

            <table>
                <tr>
                    <td></td>
                    <td>Account</td>
                    <td>Amount</td>
                </tr>
                <tr>
                    <td>*</td>
                    <td>
                        <#include "/template/transactions/account_list.ftl"/>
                    </td>
                    <td>
                        <input type="text" name="cfamount[]"/>
                    </td>
                </tr>
                <tr>
                    <td>*</td>
                    <td>
                    <#include "/template/transactions/account_list.ftl"/>
                    </td>
                    <td>
                        <input type="text" name="cfamount[]"/>
                    </td>
                </tr>
                <tr>
                    <td>*</td>
                    <td>
                    <#include "/template/transactions/account_list.ftl"/>
                    </td>
                    <td>
                        <input type="text" name="cfamount[]"/>
                    </td>
                </tr>
                <tr>
                    <td>*</td>
                    <td>
                    <#include "/template/transactions/account_list.ftl"/>
                    </td>
                    <td>
                        <input type="text" name="cfamount[]"/>
                    </td>
                </tr>
                <tr>
                    <td>*</td>
                    <td>
                    <#include "/template/transactions/account_list.ftl"/>
                    </td>
                    <td>
                        <input type="text" name="cfamount[]"/>
                    </td>
                </tr>
            </table>

            <input type="hidden" name="user.id" value="${user.id}"/>
            <input type="hidden" name="account.id" value="${account.id}"/>
            <input type="submit" value="Add Transaction"/>
        </form>
    </div>
</div>

<#include "/template/layouts/global_footer.ftl">