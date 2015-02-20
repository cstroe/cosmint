package com.github.cstroe.spendhawk.report;

import com.github.cstroe.spendhawk.report.impl.AllExpensesReport;
import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(Arquillian.class)
public class AllExpensesReportIT extends BaseIT {

    @Test
    public void testReport() throws Exception {
        startTransaction();
        ReportRunner runner = new AllExpensesReport();

        runner.getParameters().stream().forEach(p -> {
            switch(p.getId()) {
                case "startDate":
                    p.setValue("1/1/2015");
                    return;
                case "endDate":
                    p.setValue("1/31/2015");
            }
        });

        runner.runReport();
        ReportResult result = runner.getResult();

        Map<String,String> values = new HashMap<>();
        for(int i = 0; i < result.getNumRows(); i++) {
            values.put(result.getCell(i, 0), result.getCell(i, 1));
        }

        assertThat(values.keySet().size(), is(2));
        assertThat(values.get("Groceries"), is(equalTo("(91.09)")));
        assertThat(values.get("Eating Out"), is(equalTo("(14.98)")));
        commitTransaction();
    }
}
