package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.bean.TransactionManagerBean;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.repository.AccountRepository;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/transactions/add")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddTransactionServlet extends HttpServlet {
    private final AccountRepository accountRepository;
    private final TransactionManagerBean tMan;

    private static final String TEMPLATE = "/template/transactions/add.ftl";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        org.hibernate.Transaction transaction = null;
        try {
            Integer userId = Integer.parseInt(Optional.ofNullable(request.getParameter("user.id"))
                .orElseThrow(Ex::userIdRequired));
            Integer accountId = Integer.parseInt(Optional.ofNullable(request.getParameter("account.id"))
                .orElseThrow(Ex::accountIdRequired));
            // Begin unit of work
            transaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            UserDao user = null; //UserDao.findById(userId).orElseThrow(Ex::userNotFound);
            AccountDao account = accountRepository.findByIdAndUserId(accountId.longValue(), userId.longValue()).orElseThrow(Ex::accountNotFound);

            request.setAttribute("user", user);
            request.setAttribute("account", account);
            request.getRequestDispatcher(TEMPLATE).forward(request,response);
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            if(transaction != null) {
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            }
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AccountDao account;
        TransactionDao t;
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            // Handle actions
            String userIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("user.id"));
            String accountIdRaw = StringEscapeUtils.escapeHtml4(request.getParameter("account.id"));
            String dateRaw = StringEscapeUtils.escapeHtml4(request.getParameter("date"));
            String description = StringEscapeUtils.escapeHtml4(request.getParameter("description"));
            String notes = StringEscapeUtils.escapeHtml4(request.getParameter("notes"));

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormatter.parse(dateRaw);
            Integer accountId = Integer.parseInt(accountIdRaw);

            account = accountRepository.findByIdAndUserId(accountId.longValue(), null)
                .orElseThrow(Ex::accountNotFound);

//            Optional<TransactionDao> newT = tMan.createTransaction(
//                Long.parseLong(userIdRaw), date, description, notes,
//                request.getParameterValues("cfaccount[]"),
//                request.getParameterValues("cfamount[]"));

//            t = newT.get();

            //HibernateUtil.getSessionFactory().getCurrentSession().save(t);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            // End unit of work
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex.getMessage(), ex);
        }

//        response.sendRedirect(request.getContextPath() + servletPath(AccountServlet.class) +
//            "?id=" + account.getId() + "&relDate=" + "currentMonth");/
    }
}
