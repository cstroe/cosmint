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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

        createCashflows(bankAccount, 1, -10d);
        createCashflows(expenseAccount, 2, 43.11d);

        ExportBean v1 = new ExportBean();
        final String s = v1.doExportJson(mock.getMockUser());

        assertNotNull("doExportJson should not return null.", s);
        assertEquals(cashFlowExport, s);
    }

    private List<CashFlow> createCashflows(Account account, long idSeq, Double... amounts) {
        List<CashFlow> cashFlows = new ArrayList<>();
        for(Double amount : amounts) {
            CashFlow cf1 = new CashFlow();
            cf1.setId(idSeq++);
            cf1.setAccount(account);
            cf1.setAmount(amount);
            account.getCashFlows().add(cf1);
            cashFlows.add(cf1);
        }
        return cashFlows;
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

        createCashflows(bankAccount, 1, -10d);
        createCashflows(expenseAccount, 2, 43.11d);
        CashFlow outFlow = bankAccount.getCashFlows().stream().findFirst()
            .orElseThrow(() -> new AssertionError("Cannot find recorded cashflow."));
        CashFlow inFlow = expenseAccount.getCashFlows().stream().findFirst()
            .orElseThrow(() -> new AssertionError("Cannot find recorded cashflow."));

        Transaction t1 = new Transaction();
        t1.setId(1l);
        t1.setEffectiveDate(cal.getTime());
        t1.setDescription("Transaction 1.");
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