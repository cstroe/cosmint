package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.report.ReportParameter;
import com.github.cstroe.spendhawk.report.ReportResult;
import com.github.cstroe.spendhawk.report.ReportRunner;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class CategoryExpenseByMonthReport implements ReportRunner {

    private final List<ReportParameter> reportParameters;
    private ReportResult result;

    public CategoryExpenseByMonthReport() {
        List<ReportParameter> parameters = new LinkedList<>();
        parameters.add(new ReportParameter("category", "Category", ReportParameter.ReportParameterType.CATEGORY));
        parameters.add(new ReportParameter("startDate", "Start Date", ReportParameter.ReportParameterType.DATE));
        parameters.add(new ReportParameter("endDate", "End Date", ReportParameter.ReportParameterType.DATE));
        reportParameters = Collections.unmodifiableList(parameters);
    }

    @Override
    public String getName() {
        return "Single Category Expense for Date Range";
    }

    @Override
    public String getHelpfulDescription() {
        return "Calculates the expense for a single category starting from a start date to and end date.";
    }

    @Override
    public List<ReportParameter> getParameters() {
        return reportParameters;
    }

    @Override
    public void runReport() throws Exception {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy");

        SimpleReportResult result = new SimpleReportResult(1, 2);

        ReportParameter categoryParam = reportParameters.stream()
                .filter(t -> t.getId().equals("category")).findFirst().get();

        ReportParameter startDateParam = reportParameters.stream()
                .filter(t -> t.getId().equals("startDate")).findFirst().get();

        ReportParameter endDateParam = reportParameters.stream()
                .filter(t -> t.getId().equals("endDate")).findFirst().get();

        Category c = Category.findById(Long.parseLong(categoryParam.getValue()));

        Date startDate = dateFormatter.parse(startDateParam.getValue());
        Date endDate = dateFormatter.parse(endDateParam.getValue());

        @SuppressWarnings("unchecked")
        List<Expense> expenses = (List<Expense>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Expense.class)
                .add(Restrictions.eq("category", c))
                .list();

        Double sum = expenses.stream()
            .filter(ex ->
                ex.getTransaction().getEffectiveDate().after(startDate) &&
                ex.getTransaction().getEffectiveDate().before(endDate))
            .mapToDouble(Expense::getAmount).sum();

        result.setCell(0, 0, c.getName());
        result.setCell(0, 1, Double.toString(sum));

        this.result = result;
    }

    @Override
    public ReportResult getResult() {
        return result;
    }
}
