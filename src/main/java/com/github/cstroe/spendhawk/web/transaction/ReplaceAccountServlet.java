package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import com.github.cstroe.spendhawk.web.AccountServlet;

/**
 * Given a search term that finds a set of transactions, for those transactions
 * change all references for an account to point to a different account.
 *
 * This is a bulk operation, for updating the categorization of transactions.
 */
@WebServlet("/transaction/update")
@Slf4j
@Controller
@RequestMapping("/export")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReplaceAccountServlet extends HttpServlet {
    private static final String CONFIGURE_TEMPLATE = "/template/cashflow/bulk_configure.ftl";
    private static final String PREVIEW_TEMPLATE = "/template/cashflow/bulk_preview.ftl";
    private final AccountRepository accountRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer accountId = Integer.parseInt(req.getParameter("fromAccountId"));
        String queryString = Optional.ofNullable(req.getParameter("query"))
            .orElseThrow(Ex.ception("Query not defined."));
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Account account = accountRepository.findByIdAndUserId(accountId.longValue(), null)
                .orElseThrow(Ex.ception("Account not found."));
            req.setAttribute("fromAccount", account);
            req.setAttribute("query", queryString);
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
        final Integer fromAccountId = Integer.parseInt(req.getParameter("fromAccountId"));
        final String query = req.getParameter("query");
        final Integer accountToReplaceId = Integer.parseInt(req.getParameter("accountToReplaceId"));
        final Integer replacementAccountId = Integer.parseInt(req.getParameter("replacementAccountId"));

        final List<Long> cashFlowIds = Arrays.stream(req.getParameterValues("selected"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Account account = accountRepository.findByIdAndUserId(fromAccountId.longValue(), null)
            .orElseThrow(Ex::accountNotFound);

        Account toReplace = accountRepository.findByIdAndUserId(accountToReplaceId.longValue(), null)
            .orElseThrow(Ex::accountNotFound);

        Account replacement = accountRepository.findByIdAndUserId(replacementAccountId.longValue(), null)
            .orElseThrow(Ex::accountNotFound);

        req.setAttribute("fromAccountId", fromAccountId);
        req.setAttribute("account", account);
        req.setAttribute("query", query);
        req.setAttribute("toReplace", toReplace);
        req.setAttribute("replacement", replacement);
        req.setAttribute("fw", new TemplateForwarder(req));
    }

    private void doChange(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        final Integer fromAccountId = Integer.parseInt(req.getParameter("fromAccountId"));
        final Integer accountToReplaceId = Integer.parseInt(req.getParameter("accountToReplaceId"));
        final Integer replacementAccountId = Integer.parseInt(req.getParameter("replacementAccountId"));

        final List<Long> cashFlowIds = Arrays.stream(req.getParameterValues("selected[]"))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        Account account = accountRepository.findByIdAndUserId(fromAccountId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);

        Account toReplace = accountRepository.findByIdAndUserId(accountToReplaceId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);

        Account replacement = accountRepository.findByIdAndUserId(replacementAccountId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);
    }
}
