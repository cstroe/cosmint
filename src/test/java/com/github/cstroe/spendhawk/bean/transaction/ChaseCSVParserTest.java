package com.github.cstroe.spendhawk.bean.transaction;

import com.github.cstroe.spendhawk.bean.DateBean;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import org.codehaus.plexus.util.StringInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ChaseCSVParserTest {

    private ChaseCSVParser parser;

    private Account incomeAccount, expenseAccount, assetAccount, myBankAccount;

    @Before
    public void setUp() {
        parser = new ChaseCSVParser(new DateBean());

        Long accountIdSeq = 1l;

        User mockUser = new User();
        mockUser.setId(13l);

        incomeAccount = new Account();
        incomeAccount.setId(++accountIdSeq);
        incomeAccount.setName(User.DEFAULT_INCOME_ACCOUNT_NAME);
        incomeAccount.setParent(null);
        incomeAccount.setUser(mockUser);

        expenseAccount = new Account();
        expenseAccount.setId(++accountIdSeq);
        expenseAccount.setName(User.DEFAULT_EXPENSE_ACCOUNT_NAME);
        expenseAccount.setParent(null);
        expenseAccount.setUser(mockUser);

        assetAccount = new Account();
        assetAccount.setId(++accountIdSeq);
        assetAccount.setName(User.DEFAULT_ASSET_ACCOUNT_NAME);
        assetAccount.setParent(null);
        assetAccount.setUser(mockUser);

        myBankAccount = new Account();
        myBankAccount.setId(++accountIdSeq);
        myBankAccount.setName("My Bank Account");
        myBankAccount.setParent(assetAccount);
        myBankAccount.setUser(mockUser);

        List<Account> accounts = new ArrayList<>(4);
        accounts.add(incomeAccount);
        accounts.add(expenseAccount);
        accounts.add(assetAccount);
        accounts.add(myBankAccount);
        mockUser.setAccounts(accounts);
    }

    @Test
    public void debit_transaction() {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "DEBIT,08/31/2012,MARIANOS FRESH00085043 CHICAGO IL            08/30,-3.3,Testing the notes";

        List<Transaction> transactions =
            parser.parse(new StringInputStream(data), myBankAccount);

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
    public void credit_transaction() {
        String data = "Type,Post Date,Description,Amount,Check or Slip #\n" +
                "CREDIT,08/01/2012,\"INCOME SOURCE\",1234.56,";

        List<Transaction> transactions =
                parser.parse(new StringInputStream(data), myBankAccount);

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
}