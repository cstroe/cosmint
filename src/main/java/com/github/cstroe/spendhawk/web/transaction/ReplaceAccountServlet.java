package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.bean.BulkUpdateBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import com.github.cstroe.spendhawk.web.AccountServlet;
import org.apache.commons.lang3.StringEscapeUtils;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

/**
 * Given a search term that finds a set of transactions, for those transactions
 * change all references for an account to point to a different account.
 *
 * This is a bulk operation, for updating the categorization of transactions.
 */
@WebServlet("/transaction/update")
public class ReplaceAccountServlet extends HttpServlet {
    private static final String CONFIGURE_TEMPLATE = "/template/cashflow/bulk_configure.ftl";
    private static final String PREVIEW_TEMPLATE = "/template/cashflow/bulk_preview.ftl";

    @Inject
    private BulkUpdateBean buBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long accountId = Long.parseLong(req.getParameter("fromAccountId"));
        String queryString = Optional.ofNullable(req.getParameter("query"))
            .orElseThrow(Ex.ception("Query not defined."));
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Account account = Account.findById(accountId)
                .orElseThrow(Ex.ception("Account not found."));
            List<CashFlow> relevantCashFlows = account.findCashFlows(queryString);
            Collection<Account> allAccounts = buBean.getAccounts(relevantCashFlows);
            Collection<Account> relevantAccounts = allAccounts
                .stream().filter(a -> !a.equals(account)).collect(Collectors.toList());
            req.setAttribute("fromAccount", account);
            req.setAttribute("query", queryString);
            req.setAttribute("cashflows", relevantCashFlows);
            req.setAttribute("accountsToReplace", relevantAccounts);
            req.setAttribute("accountsAll", account.getUser().getAccounts());
            req.setAttribute("fw", new TemplateForwarder(req));
            req.getRequestDispatcher(CONFIGURE_TEMPLATE).forward(req,resp);
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String action = StringEscapeUtils.escapeHtml4(req.getParameter("action"));
        if("Preview".equals(action)) {
            doPreview(req, resp);
        } else if("Apply changes".equals(action)) {
            doChange(req, resp);
        }
    }

    private void doPreview(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Long fromAccountId = Long.parseLong(req.getParameter("fromAccountId"));
        final String query = req.getParameter("query");
        final Long accountToReplaceId = Long.parseLong(req.getParameter("accountToReplaceId"));
        final Long replacementAccountId = Long.parseLong(req.getParameter("replacementAccountId"));

        final List<Long> cashFlowIds = Arrays.asList(req.getParameterValues("selected"))
            .stream().map(Long::parseLong).collect(Collectors.toList());

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Account account = Account.findById(fromAccountId)
                .orElseThrow(Ex::accountNotFound);

            Account toReplace = Account.findById(accountToReplaceId)
                .orElseThrow(Ex::accountNotFound);

            Account replacement = Account.findById(replacementAccountId)
                .orElseThrow(Ex::accountNotFound);

            @SuppressWarnings("RedundantTypeArguments") // see https://bugs.openjdk.java.net/browse/JDK-8054569
            List<CashFlow> cashflows = cashFlowIds.stream()
                .map(id -> CashFlow.findById(id).<RuntimeException>orElseThrow(Ex::cashFlowNotFound))
                .collect(Collectors.toList());

            Collection<CashFlow> updatedCashFlows = buBean.previewReplace(cashflows, toReplace, replacement);

            req.setAttribute("fromAccountId", fromAccountId);
            req.setAttribute("account", account);
            req.setAttribute("query", query);
            req.setAttribute("toReplace", toReplace);
            req.setAttribute("replacement", replacement);
            req.setAttribute("cashflows", updatedCashFlows);
            req.setAttribute("fw", new TemplateForwarder(req));
            req.getRequestDispatcher(PREVIEW_TEMPLATE).forward(req, resp);

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    private void doChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Long fromAccountId = Long.parseLong(req.getParameter("fromAccountId"));
        final Long accountToReplaceId = Long.parseLong(req.getParameter("accountToReplaceId"));
        final Long replacementAccountId = Long.parseLong(req.getParameter("replacementAccountId"));

        final List<Long> cashFlowIds = Arrays.asList(req.getParameterValues("selected[]"))
                .stream().map(Long::parseLong).collect(Collectors.toList());

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            @SuppressWarnings("RedundantTypeArguments") // see https://bugs.openjdk.java.net/browse/JDK-8054569
                    List<CashFlow> cashflows = cashFlowIds.stream()
                    .map(id -> CashFlow.findById(id).<RuntimeException>orElseThrow(Ex::cashFlowNotFound))
                    .collect(Collectors.toList());

            Account account = Account.findById(fromAccountId)
                    .orElseThrow(Ex::accountNotFound);

            Account toReplace = Account.findById(accountToReplaceId)
                    .orElseThrow(Ex::accountNotFound);

            Account replacement = Account.findById(replacementAccountId)
                    .orElseThrow(Ex::accountNotFound);


            Collection<CashFlow> updatedCashFlows = buBean.previewReplace(cashflows, toReplace, replacement);

            for (CashFlow updatedCashFlow : updatedCashFlows) {
                updatedCashFlow.save();
            }

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();

            resp.sendRedirect(req.getContextPath() + servletPath(AccountServlet.class, "id", account.getId(), "relDate", "currentMonth"));
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
