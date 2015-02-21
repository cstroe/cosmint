package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.helper.TListTotaler;
import com.github.cstroe.spendhawk.util.DateUtil;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {

    public static final String TEMPLATE = "/template/account.ftl";

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            LocalDate startDate = null;
            LocalDate endDate = null;

            String accountIdRaw = request.getParameter("id");
            String startDateRaw = request.getParameter("start");
            String endDateRaw = request.getParameter("end");
            String relDateRaw = request.getParameter("relDate");

            if(startDateRaw != null && endDateRaw != null) {
                startDate = formatter.parse(startDateRaw, LocalDate::from);
                endDate = formatter.parse(endDateRaw, LocalDate::from);
            }


            if(relDateRaw != null) {
                if("currentMonth".equals(relDateRaw)) {
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = null;
                } else {
                    LocalDate date = formatter.parse(relDateRaw, LocalDate::from);
                    startDate = date.withDayOfMonth(1);
                    endDate = date.with(TemporalAdjusters.lastDayOfMonth());
                }
            }

            if(accountIdRaw != null) {
                Long accountId = Long.parseLong(accountIdRaw);
                // Begin unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
                Account account = Account.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found."));
                if(account == null) {
                    throw new IllegalArgumentException("Account not found.");
                }

                Criteria query = HibernateUtil.getSessionFactory()
                    .getCurrentSession()
                    .createCriteria(Transaction.class)
                    .add(Restrictions.eq("account", account));

                if(startDate != null) {
                    query.add(Restrictions.ge("effectiveDate", DateUtil.asDate(startDate)));
                }

                if(endDate != null) {
                    query.add(Restrictions.le("effectiveDate", DateUtil.asDate(endDate)));
                }

                @SuppressWarnings("unchecked")
                List<Transaction> result = (List<Transaction>) query.addOrder(Order.desc("effectiveDate")).list();

                request.setAttribute("account", account);
                request.setAttribute("transactions", result);
                request.setAttribute("totaler", new TListTotaler(result));
                setNavigationDates(request, startDate);
                request.getRequestDispatcher(TEMPLATE).forward(request,response);
                // End unit of work
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
            }
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    public static void setNavigationDates(HttpServletRequest request, LocalDate monthStartDate) {
        request.setAttribute("previousMonth", formatter.format(monthStartDate.minusMonths(1).withDayOfMonth(1)));
        request.setAttribute("nextMonth", formatter.format(monthStartDate.plusMonths(1).withDayOfMonth(1)));
    }
}
