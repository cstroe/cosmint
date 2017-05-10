package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.DateUtil;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
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
            LocalDate startDate;
            LocalDate endDate;

            Long accountId = Long.parseLong(request.getParameter("id"));
            String startDateRaw = request.getParameter("start");
            String endDateRaw = request.getParameter("end");
            String relDateRaw = request.getParameter("relDate");

            if(startDateRaw != null && endDateRaw != null) {
                startDate = formatter.parse(startDateRaw, LocalDate::from);
                endDate = formatter.parse(endDateRaw, LocalDate::from);
            } else if(relDateRaw != null) {
                if("currentMonth".equals(relDateRaw)) {
                    startDate = LocalDate.now().withDayOfMonth(1);
                    endDate = null;
                } else if("allTime".equals(relDateRaw)) {
                    startDate = LocalDate.of(1900,1,1);
                    endDate = null;
                } else {
                    LocalDate date = formatter.parse(relDateRaw, LocalDate::from);
                    startDate = date.withDayOfMonth(1);
                    endDate = date.with(TemporalAdjusters.lastDayOfMonth());
                }
            } else {
                throw new IllegalAccessException("Dates could not be parsed.");
            }

            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            Account account = Account.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found."));
            if(account == null) {
                throw new IllegalArgumentException("Account not found.");
            }

            Criteria query = HibernateUtil.getSessionFactory()
                .getCurrentSession()
                .createCriteria(CashFlow.class)
                    .add(Restrictions.eq("account", account));

            if(startDate != null) {
                query.add(Restrictions.ge("effectiveDate", DateUtil.asDate(startDate)));
            }

            if(endDate != null) {
                query.add(Restrictions.le("effectiveDate", DateUtil.asDate(endDate)));
            }

            @SuppressWarnings("unchecked")
            List<CashFlow> result = (List<CashFlow>) query.addOrder(Order.desc("effectiveDate")).list();

            request.setAttribute("account", account);
            request.setAttribute("cashflows", result);
            request.setAttribute("fw", new TemplateForwarder(request));
            setNavigationDates(request, startDate);
            request.getRequestDispatcher(TEMPLATE).forward(request,response);
            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            org.hibernate.Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().getTransaction();
            //if(t.isActive()) {
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            //}
            throw new ServletException(ex);
        }
    }

    public static void setNavigationDates(HttpServletRequest request, LocalDate monthStartDate) {
        request.setAttribute("previousMonth", formatter.format(monthStartDate.minusMonths(1).withDayOfMonth(1)));
        request.setAttribute("nextMonth", formatter.format(monthStartDate.plusMonths(1).withDayOfMonth(1)));
    }
}
