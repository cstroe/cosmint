package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
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

        recordAnExpense(bankAccount, expenseAccount);

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }

    private void recordAnExpense(Account bankAccount, Account expenseAccount) {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(2012, 1, 1);

        {
            CashFlow cf1 = new CashFlow();
            cf1.setId(1l);
            cf1.setAccount(bankAccount);
            cf1.setAmount(-10.00d);
            bankAccount.getCashFlows().add(cf1);
        }{
            CashFlow cf2 = new CashFlow();
            cf2.setId(2l);
            cf2.setAccount(expenseAccount);
            cf2.setAmount(-43.11d);
            cal.set(2013,12,31);
            expenseAccount.getCashFlows().add(cf2);
        }
    }
}