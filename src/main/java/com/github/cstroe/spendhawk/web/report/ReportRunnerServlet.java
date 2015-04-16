package com.github.cstroe.spendhawk.web.report;

import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.report.ReportFormGenerator;
import com.github.cstroe.spendhawk.report.ReportResultRenderer;
import com.github.cstroe.spendhawk.report.ReportRunner;
import com.github.cstroe.spendhawk.report.impl.TableRenderer;
import com.github.cstroe.spendhawk.util.Exceptions;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.github.cstroe.spendhawk.util.TemplateForwarder;
import org.apache.commons.lang3.StringEscapeUtils;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@WebServlet("/report")
public class ReportRunnerServlet extends HttpServlet {

    private static final String TEMPLATE_SELECT_REPORT = "/template/report/runner_select_report.ftl";
    private static final String TEMPLATE_ENTER_PARAMETERS = "/template/report/runner_enter_parameters.ftl";
    private static final String TEMPLATE_RESULT = "/template/report/runner_result.ftl";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userId = StringEscapeUtils.escapeHtml4(req.getParameter("user.id"));

        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            User currentUser = User.findById(Long.parseLong(userId))
                .orElseThrow(Exceptions::userNotFound);
            req.setAttribute("user", currentUser);
            req.setAttribute("reports", getReports(currentUser));
            req.getRequestDispatcher(TEMPLATE_SELECT_REPORT).forward(req, resp);
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch (Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = StringEscapeUtils.escapeHtml4(req.getParameter("action"));
        String reportName = StringEscapeUtils.escapeHtml4(req.getParameter("report.name"));
        String userId = StringEscapeUtils.escapeHtml4(req.getParameter("user.id"));
        try {
            HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            User currentUser = User.findById(Long.parseLong(userId))
                .orElseThrow(Exceptions::userNotFound);
            ReportRunner report = getReportByName(currentUser, reportName)
                .orElseThrow(Exceptions::reportNotFound);
            ReportFormGenerator rfg =
                new ReportFormGenerator(currentUser, report.getParameters());

            if ("Enter Parameters".equals(action)) {
                req.setAttribute("report", report);
                req.setAttribute("generate", rfg);
                req.setAttribute("user", currentUser);
                req.getRequestDispatcher(TEMPLATE_ENTER_PARAMETERS).forward(req, resp);
            } else if("Run Report".equals(action)) {
                rfg.parseParameters(req, report.getParameters());
                report.runReport();
                ReportResultRenderer renderer = new TableRenderer(report.getResult());
                req.setAttribute("fw", new TemplateForwarder(req));
                req.setAttribute("user", currentUser);
                req.setAttribute("renderer", renderer);
                req.getRequestDispatcher(TEMPLATE_RESULT).forward(req, resp);
            }
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().commit();
        } catch(Exception ex) {
            HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().rollback();
            throw new ServletException(ex);
        }
    }

    private List<ReportRunner> getReports(User currentUser) {
        Reflections reflections = new Reflections(ClasspathHelper.forPackage("com.github.cstroe.spendhawk"), new SubTypesScanner());
        Set<Class<? extends ReportRunner>> reportClasses = reflections.getSubTypesOf(ReportRunner.class);

        List<ReportRunner> reportRunners = new LinkedList<>();
        for (Class<? extends ReportRunner> reportClass : reportClasses) {
            try {
                Constructor<? extends ReportRunner> reportRunnerConstructor =
                    reportClass.getConstructor(User.class);
                ReportRunner report = reportRunnerConstructor.newInstance(currentUser);
                reportRunners.add(report);
            } catch( InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        return reportRunners;
    }

    private Optional<ReportRunner> getReportByName(User currentUser, String name) {
        List<ReportRunner> reports = getReports(currentUser);
        for(ReportRunner report : reports) {
            if(report.getName().equals(name)) {
                return Optional.of(report);
            }
        }
        return Optional.empty();
    }
}
