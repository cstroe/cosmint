package com.github.cstroe.spendhawk.web.transaction;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import org.hibernate.Session;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/transaction/search")
public class TransactionSearchServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/transactions/search.ftl";

    private AccountRepository accountRepository;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String searchString = req.getParameter("q");
        String accountId = req.getParameter("account.id");

        Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            currentSession.beginTransaction();
            Account account = accountRepository.findByIdAndUserId(Long.parseLong(accountId), null)
                .orElseThrow(Ex::accountNotFound);
            req.setAttribute("account", account);
            req.setAttribute("query", searchString);
            //Collection<CashFlow> tList = account.findCashFlows(searchString);
            //req.setAttribute("cashflows", tList);
            req.setAttribute("fw", new TemplateForwarder(req));
            req.getRequestDispatcher(TEMPLATE).forward(req,resp);
            currentSession.getTransaction().commit();
        } catch(Exception ex) {
            currentSession.getTransaction().rollback();
            throw ex;
        }
    }
}
