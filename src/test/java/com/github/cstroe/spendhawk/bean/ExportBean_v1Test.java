package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.TransactionDao;
import com.github.cstroe.spendhawk.json.ExportBean;
import com.github.cstroe.spendhawk.mocks.SeedAccounts;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ExportBean_v1Test {

    @Ignore
    @Test
    public void do_bare_export() throws Exception {
        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/bare.json");
        final String bareExport = IOUtils.toString(bareExportStream);

        SeedAccounts mock = new SeedAccounts();
        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(bareExport, s);
    }

    @Ignore
    @Test
    public void do_cashflow_export() throws Exception {
        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/cashflow.json");
        final String cashFlowExport = IOUtils.toString(bareExportStream);

        SeedAccounts mock = new SeedAccounts();
        AccountDao bankAccount = mock.getMyBankAccount();
//        bankAccount.setTransactions(new LinkedList<>());
        AccountDao expenseAccount = mock.getExpenseAccount();
//        expenseAccount.setCashFlows(new LinkedList<>());

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }

    @Ignore
    @Test
    public void do_transaction_export() throws Exception {
        Calendar cal = new GregorianCalendar();
        cal.clear();
        cal.set(2012, Calendar.JANUARY, 1);

        final InputStream bareExportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("export/v1/transaction.json");
        final String cashFlowExport = IOUtils.toString(bareExportStream);

        SeedAccounts mock = new SeedAccounts();
        AccountDao bankAccount = mock.getMyBankAccount();
//        bankAccount.setCashFlows(new LinkedList<>());
        AccountDao expenseAccount = mock.getExpenseAccount();
//        expenseAccount.setCashFlows(new LinkedList<>());

        TransactionDao t1 = new TransactionDao();
        t1.setId(1);
//        t1.setNotes("Notes.");

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }
}