package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.json.ExportBean;
import com.github.cstroe.spendhawk.mocks.BareAccountsMock;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ExportBean_v1Test {

    @Test
    public void do_bare_export() throws Exception {
        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/bare.json");
        final String bareExport = IOUtils.toString(bareExportStream);

        BareAccountsMock mock = new BareAccountsMock();
        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(bareExport, s);
    }

    @Test
    public void do_cashflow_export() throws Exception {
        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/cashflow.json");
        final String cashFlowExport = IOUtils.toString(bareExportStream);

        BareAccountsMock mock = new BareAccountsMock();
        Account bankAccount = mock.getMyBankAccount();
        bankAccount.setCashFlows(new LinkedList<>());
        Account expenseAccount = mock.getExpenseAccount();
        expenseAccount.setCashFlows(new LinkedList<>());

        createCashflow(bankAccount, 1, "01/01/2012", -10d, "Description 1");
        createCashflow(expenseAccount, 2, "01/02/2012", 43.11d, "Description 2");

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }

    private CashFlow createCashflow(Account account, long id, String effectiveDate, Double amount, String description) {
        DateBean dateBean = new DateBean();
        CashFlow cf1 = new CashFlow();
        cf1.setId(id);
        cf1.setAccount(account);
        cf1.setAmount(amount);
        if(effectiveDate != null) {
            cf1.setEffectiveDate(dateBean.parse(effectiveDate));
        }
        cf1.setDescription(description);
        account.getCashFlows().add(cf1);
        return cf1;
    }

    @Test
    public void do_transaction_export() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(2012, Calendar.JANUARY, 1);

        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/transaction.json");
        final String cashFlowExport = IOUtils.toString(bareExportStream);

        BareAccountsMock mock = new BareAccountsMock();
        Account bankAccount = mock.getMyBankAccount();
        bankAccount.setCashFlows(new LinkedList<>());
        Account expenseAccount = mock.getExpenseAccount();
        expenseAccount.setCashFlows(new LinkedList<>());

        CashFlow outFlow = createCashflow(bankAccount, 1, "01/01/2012",  -10d, "Description 1");
        CashFlow inFlow = createCashflow(expenseAccount, 2, "01/02/2012", 43.11d, "Description 2");

        Transaction t1 = new Transaction();
        t1.setId(1l);
        t1.setNotes("Notes.");
        t1.getCashFlows().add(outFlow);
        outFlow.setTransaction(t1);
        t1.getCashFlows().add(inFlow);
        inFlow.setTransaction(t1);

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }
}