package com.github.cstroe.spendhawk.web;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.json.ExportBean;
import com.github.cstroe.spendhawk.util.Ex;
import com.github.cstroe.spendhawk.util.HibernateUtil;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

@WebServlet("/export")
public class ExportServlet extends HttpServlet {

    @Inject
    private ExportBean exportBean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            Long userId = Long.parseLong(req.getParameter("user.id"));

            // Begin unit of work
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();

            User currentUser = User.findById(userId).orElseThrow(Ex::userNotFound);

            resp.setContentType("application/x-gzip");
            resp.setHeader("Content-Disposition", "filename=\"" + currentUser.getName() + "-" + getDateTag() + ".json.gz\"");

            exportBean.doExportJson(currentUser, new GZIPOutputStream(resp.getOutputStream()));

            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch(Exception ex) {
            org.hibernate.Transaction t = HibernateUtil.getSessionFactory().getCurrentSession().getTransaction();
            if(t.isActive()) {
                HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            }

            throw new ServletException(ex);
        }
    }

    private String getDateTag() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}
