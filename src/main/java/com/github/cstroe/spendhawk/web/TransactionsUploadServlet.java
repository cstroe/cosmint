package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.bean.transaction.ChaseCSVParser;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/transactions/upload")
@MultipartConfig
public class TransactionsUploadServlet extends HttpServlet {
    private AccountRepository accountRepository;

    private static final String TEMPLATE = "/template/transactions/upload.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Integer accountId = Integer.parseInt(req.getParameter("id"));
            AccountDao account = accountRepository.findByIdAndUserId(accountId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);
            req.setAttribute("messages", new LinkedList<String>());
            req.setAttribute("account", account);

            req.getRequestDispatcher(TEMPLATE).forward(req, resp);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create path components to save the file
        final String fileFormat = request.getParameter("format");
        final Part filePart = request.getPart("file");
        final Integer accountId = Integer.parseInt(request.getParameter("id"));
        //final boolean duplicateCheck = request.getParameter("duplicate_check") != null;

        try {
            List<String> messages = new ArrayList<>();
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            InputStream filecontent = filePart.getInputStream();
            AccountDao account = accountRepository.findByIdAndUserId(accountId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);

//            AccountDao incomeAccount = account.getUser().getDefaultIncomeAccount()
//                .orElseGet(() -> {
//                    AccountDao ic = new AccountDao();
//                    ic.setName(UserDao.DEFAULT_INCOME_ACCOUNT_NAME);
//                    ic.setUser(account.getUser());
//                    ic.save();
//                    return ic;
//                });
//
//            AccountDao expenseAccount = account.getUser().getDefaultExpenseAccount()
//                .orElseGet(() -> {
//                    AccountDao ec = new AccountDao();
//                    ec.setName(UserDao.DEFAULT_EXPENSE_ACCOUNT_NAME);
//                    ec.setUser(account.getUser());
//                    ec.save();
//                    return ec;
//                });

            if("chase".equals(fileFormat)) {
                DateBean dateBean = new DateBean();
                ChaseCSVParser parser = new ChaseCSVParser(dateBean);
//                List<TransactionDao> transactions = parser.parse(filecontent, account, incomeAccount, expenseAccount);
//                for(TransactionDao t : transactions) {
//                    t.save();
//                    for(CashFlow f : t.getCashFlows()) {
//                        f.save();
//                    }
//                }
//                messages.add("Created " + transactions.size() + " transactions.");
            }

            request.setAttribute("account", account);
            request.setAttribute("messages", messages);
            request.getRequestDispatcher(TEMPLATE).forward(request, response);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }
}
