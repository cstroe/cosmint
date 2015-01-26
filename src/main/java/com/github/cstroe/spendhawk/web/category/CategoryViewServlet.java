package com.github.cstroe.spendhawk.web.category;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.web.user.UserSummaryServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static com.github.cstroe.spendhawk.util.ServletUtil.servletPath;

@WebServlet("/category")
public class CategoryViewServlet extends HttpServlet {

    private static final String TEMPLATE = "/template/category/view.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryId = req.getParameter("id");
        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            Category currentCategory = Category.findById(Long.parseLong(categoryId));
            req.setAttribute("category", currentCategory);
            req.getRequestDispatcher(TEMPLATE).forward(req, resp);

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String categoryId = req.getParameter("category.id");

        try {
            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            if("Delete".equals(action)) {
                Category currentCategory = Category.findById(Long.parseLong(categoryId));

                User currentUser = currentCategory.getUser();

                List<Expense> expenseList = Expense.findByCategory(currentCategory);
                for (Expense expense : expenseList) {
                    expense.delete();
                }
                currentCategory.delete();

                resp.sendRedirect(req.getContextPath() + servletPath(UserSummaryServlet.class) +
                    "?user.id=" + currentUser.getId().toString());
            }

            // End unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw ex;
        }
    }
}
