package com.github.cstroe.spendhawk.report.impl;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.report.ReportParameter;
import com.github.cstroe.spendhawk.report.ReportResult;
import com.github.cstroe.spendhawk.report.ReportRunner;
import com.github.cstroe.spendhawk.util.DateUtil;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.criterion.Restrictions;

import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("unused")
public class AllExpensesReport implements ReportRunner {

    // because this class is not container managed, we can't use CDI annotations
    DateBean dateBean = new DateBean();

    private final List<ReportParameter> reportParameters;
    private SimpleReportResult result;

    public AllExpensesReport() {
        List<ReportParameter> parameters = new LinkedList<>();
        parameters.add(rp("startDate", "Start Date", ReportParameter.ReportParameterType.DATE));
        parameters.add(rp("endDate", "End Date", ReportParameter.ReportParameterType.DATE));
        reportParameters = Collections.unmodifiableList(parameters);
    }

    @Override
    public String getName() {
        return "All Expenses by Time Range";
    }

    @Override
    public String getHelpfulDescription() {
        return "Calculate the totals of all the expenses for a range of dates.";
    }

    @Override
    public List<ReportParameter> getParameters() {
        return reportParameters;
    }

    @Override
    public void runReport() throws Exception {
        ReportParameter startDateParam = reportParameters.stream()
                .filter(t -> t.getId().equals("startDate")).findFirst().get();

        ReportParameter endDateParam = reportParameters.stream()
                .filter(t -> t.getId().equals("endDate")).findFirst().get();

        Date startDate = dateBean.parse(startDateParam.getValue());
        Date endDate = dateBean.parse(endDateParam.getValue());

        // move date up by one day to make search inclusive to end date
        endDate = DateUtil.addDays(endDate, 1);

        @SuppressWarnings("unchecked")
        List<Expense> expenses =
            (List<Expense>) HibernateUtil.getSessionFactory().getCurrentSession()
                .createCriteria(Expense.class)
                .createCriteria("transaction")
                    .add(Restrictions.ge("effectiveDate", startDate))
                    .add(Restrictions.lt("effectiveDate", endDate))
                .list();

        HashMap<Category, Double> expenseTotalsByCategory = new HashMap<>();
        expenses.stream().forEach(e -> {
            Double categoryTotal;
            if(expenseTotalsByCategory.containsKey(e.getCategory())) {
                categoryTotal = expenseTotalsByCategory.get(e.getCategory());
                categoryTotal += e.getAmount();
            } else {
                categoryTotal = e.getAmount();
            }
            expenseTotalsByCategory.put(e.getCategory(), categoryTotal);
        });

        result = new SimpleReportResult(expenseTotalsByCategory.keySet().size(), 2);

        // sort the entries by amount
        List<Map.Entry<Category, Double>> sortedEntries = new LinkedList<>();
        sortedEntries.addAll(expenseTotalsByCategory.entrySet());
        sortedEntries.sort(Comparator.comparingDouble(Map.Entry::getValue));

        // format the decimals
        DecimalFormat fmt = new DecimalFormat("#,##0.00;(#,##0.00)");

        int i = 0;
        for(Map.Entry<Category, Double> entry : sortedEntries) {
            result.setCell(i, 0, entry.getKey().getName());
            result.setCell(i, 1, fmt.format(entry.getValue()));
            i++;
        }
    }

    @Override
    public ReportResult getResult() {
        return result;
    }
}
