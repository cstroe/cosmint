package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/accounts")
public class AccountManagerServlet extends HttpServlet {
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            // Process request and render page...
            // Write HTML header
            PrintWriter out = response.getWriter();
            printHeader(out);
            printAccountForm(out);
            listAccounts(out);
            printFooter(out);
            out.flush();
            out.close();

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            PrintWriter out = response.getWriter();
            printHeader(out);
            // Handle actions
            if ( "store".equals(request.getParameter("action")) ) {

                String accountName = StringEscapeUtils.escapeHtml4(request.getParameter("accountName"));

                if ("".equals(accountName)) {
                    out.println("<b><i>Please enter account name.</i></b>");
                }
                else {
                    createAndStoreAccount(accountName);
                    out.println("<b><i>Added account.</i></b>");
                }
            }
            printAccountForm(out);
            listAccounts(out);
            printFooter(out);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    private void printHeader(PrintWriter out) {
        out.println("<html><head><title>Account Manager</title></head><body>");
    }

    private void printFooter(PrintWriter out) {
        out.println("</body></html>");
    }
    private void printAccountForm(PrintWriter out) {
        out.println("<h2>Add new account:</h2>");
        out.println("<form method='POST'>");
        out.println("Name: <input name='accountName' length='50'/><br/>");
        out.println("<input type='submit' name='action' value='store'/>");
        out.println("</form>");
    }

    private void listAccounts(PrintWriter out) {

        List result = HibernateUtil.getSessionFactory()
                .getCurrentSession().createCriteria(Account.class).list();
        if (result.size() > 0) {
            out.println("<h2>Accounts in database:</h2>");
            out.println("<table border='1'>");
            out.println("<tr>");
            out.println("<th>Account Name</th>");
            out.println("<th>Account Balance</th>");
            out.println("</tr>");
            for(Object aResult : result) {
                Account account = (Account) aResult;
                out.println("<tr>");
                out.println("<td>" + account.getName() + "</td>");
                out.println("<td>" + account.getBalance() + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
        }
    }

    protected void createAndStoreAccount(String accountName) {
        Account theAccount = new Account();
        theAccount.setName(accountName);

        HibernateUtil.getSessionFactory()
                .getCurrentSession().save(theAccount);
    }
}
