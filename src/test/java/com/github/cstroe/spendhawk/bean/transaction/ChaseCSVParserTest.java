package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.mocks.BareAccountsMock;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChaseCSVParserTest {

    private ChaseCSVParser parser;

    @SuppressWarnings("FieldCanBeLocal")
    private Account incomeAccount, expenseAccount, assetAccount, myBankAccount;

    @Before
    public void setUp() {
        parser = new ChaseCSVParser(new DateBean());

        BareAccountsMock mock = new BareAccountsMock();
        incomeAccount = mock.getIncomeAccount();
        expenseAccount = mock.getExpenseAccount();
        assetAccount = mock.getAssetAccount();
        myBankAccount = mock.getMyBankAccount();
    }

    @Test
    public void debit_transaction() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DEBIT,08/31/2012,MARIANOS FRESH00085043 CHICAGO IL            08/30,-3.3,Testing the notes";

        List<Transaction> transactions =
            parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);

        assertThat(transactions.size(), is(1));

        Transaction generatedTransaction = transactions.get(0);

        assertThat(generatedTransaction.getDescription(),
            is(equalTo("MARIANOS FRESH00085043 CHICAGO IL            08/30")));
        assertThat(generatedTransaction.getNotes(),
            is(equalTo("Testing the notes")));
        assertThat(generatedTransaction.getCashFlows().size(), is(2));

        Date effectiveDate = generatedTransaction.getEffectiveDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);

        assertThat(cal.get(Calendar.YEAR), is(equalTo(2012)));
        assertThat(cal.get(Calendar.MONTH), is(equalTo(Calendar.AUGUST)));
        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(equalTo(31)));

        CashFlow accountCf = generatedTransaction.getCashFlows().stream()
            .filter(cf -> cf.getAccount().equals(myBankAccount))
            .findFirst()
            .orElseThrow(() -> new AssertionError("There must exist a cashflow for the given account."));

        assertThat(accountCf.getAmount(), is(equalTo(-3.3d)));

        CashFlow expenseCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(expenseAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the default expense account."));

        assertThat(expenseCf.getAmount(), is(equalTo(3.3d)));
    }

    @Test
    public void credit_transaction() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "CREDIT,08/01/2012,\"INCOME SOURCE\",1234.56,";

        List<Transaction> transactions =
                parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);

        assertThat(transactions.size(), is(1));

        Transaction generatedTransaction = transactions.get(0);

        assertThat(generatedTransaction.getDescription(),
                is(equalTo("INCOME SOURCE")));
        assertThat(generatedTransaction.getNotes(),
                is(equalTo("")));
        assertThat(generatedTransaction.getCashFlows().size(), is(2));

        Date effectiveDate = generatedTransaction.getEffectiveDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);

        assertThat(cal.get(Calendar.YEAR), is(equalTo(2012)));
        assertThat(cal.get(Calendar.MONTH), is(equalTo(Calendar.AUGUST)));
        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(equalTo(1)));

        CashFlow accountCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(myBankAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the given account."));

        assertThat(accountCf.getAmount(), is(equalTo(1234.56d)));

        CashFlow expenseCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(incomeAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the default income account."));

        assertThat(expenseCf.getAmount(), is(equalTo(-1234.56d)));
    }

    @Test
    public void check_transaction() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
            "CHECK,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,-47.96,444";

        List<Transaction> transactions =
                parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);

        assertThat(transactions.size(), is(1));

        Transaction generatedTransaction = transactions.get(0);

        assertThat(generatedTransaction.getDescription(),
                is(equalTo("CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX")));
        assertThat(generatedTransaction.getNotes(),
                is(equalTo("444")));
        assertThat(generatedTransaction.getCashFlows().size(), is(2));

        Date effectiveDate = generatedTransaction.getEffectiveDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);

        assertThat(cal.get(Calendar.YEAR), is(equalTo(2015)));
        assertThat(cal.get(Calendar.MONTH), is(equalTo(Calendar.JANUARY)));
        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(equalTo(20)));

        CashFlow accountCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(myBankAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the given account."));

        assertThat(accountCf.getAmount(), is(equalTo(-47.96d)));

        CashFlow expenseCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(expenseAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the default income account."));

        assertThat(expenseCf.getAmount(), is(equalTo(47.96d)));
    }

    @Test
    public void check_deposit_slip() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DSLIP,02/03/2015,DEPOSIT  ID NUMBER XXXXX,200,";

        List<Transaction> transactions =
                parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);

        assertThat(transactions.size(), is(1));

        Transaction generatedTransaction = transactions.get(0);

        assertThat(generatedTransaction.getDescription(),
                is(equalTo("DEPOSIT  ID NUMBER XXXXX")));
        assertThat(generatedTransaction.getNotes(),
                is(equalTo("")));
        assertThat(generatedTransaction.getCashFlows().size(), is(2));

        Date effectiveDate = generatedTransaction.getEffectiveDate();
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);

        assertThat(cal.get(Calendar.YEAR), is(equalTo(2015)));
        assertThat(cal.get(Calendar.MONTH), is(equalTo(Calendar.FEBRUARY)));
        assertThat(cal.get(Calendar.DAY_OF_MONTH), is(equalTo(3)));

        CashFlow accountCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(myBankAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the given account."));

        assertThat(accountCf.getAmount(), is(equalTo(200.00d)));

        CashFlow expenseCf = generatedTransaction.getCashFlows().stream()
                .filter(cf -> cf.getAccount().equals(incomeAccount))
                .findFirst()
                .orElseThrow(() -> new AssertionError("There must exist a cashflow for the default income account."));

        assertThat(expenseCf.getAmount(), is(equalTo(-200.00d)));
    }

    @Test(expected = Exception.class)
    public void fail_on_invalid_type() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
            "WRONG,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,-47.96,444";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }

    @Test(expected = Exception.class)
    public void fail_on_amount_is_zero() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DEBIT,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,0.00,444";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }

    @Test(expected = Exception.class)
    public void fail_on_amount_is_negative_zero() throws Exception {
        // see: http://stackoverflow.com/questions/6724031/how-can-a-primitive-float-value-be-0-0-what-does-that-mean/8153449#8153449
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DEBIT,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,-0.00,444";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }

    @Test(expected = Exception.class)
    public void fail_on_positive_amount_for_debit() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
            "DEBIT,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,47.96,444";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }

    @Test(expected = Exception.class)
    public void fail_on_negative_amount_for_credit() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
            "CREDIT,01/20/2015,CHECK # 0444      BILL ME LATER    PAYMENT           ARC ID: XXXXXXXXXXXXXX,-47.96,444";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }

    @Test(expected = Exception.class)
    public void fail_on_negative_amount_for_deposit_slip() throws Exception {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DSLIP,02/03/2015,DEPOSIT  ID NUMBER XXXXX,-200,";

        parser.parse(new ByteArrayInputStream(data.getBytes()), myBankAccount, incomeAccount, expenseAccount);
    }
}