package com.github.cstroe.spendhawk.entity;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class AccountTest {

    private List<Account> accountsList = new ArrayList<>();
    private List<Transaction> transactionList = new ArrayList<>();

    private String[] accountsTemplate = {
            // Account ID, Account Name
            "1", "Account1"
    };

    private String[] transactionsTemplate = {
            // Transaction ID, Account ID, Amount, Date ("yyMMddHHmmss"), Description
            "1", "1", "10.99", "010101000000", "Transaction 1",
            "1", "1", "11.40", "010101000000", "Transaction 2"
    };

    private void createAccounts() {
        try {
            for (int i = 0; i < accountsTemplate.length; i += 2) {
                Long accountId = Long.parseLong(accountsTemplate[i]);
                String accountName = accountsTemplate[i+1];

                // create account
                Account account = new Account();

                // set account properties
                Field idField = account.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(account, accountId);
                idField.setAccessible(false);
                account.setName(accountName);
                account.setTransactions(new ArrayList<>());
                accountsList.add(account);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void createTransactions() {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
            for (int i = 0; i < transactionsTemplate.length; i += 5) {
                Long transactionId = Long.parseLong(transactionsTemplate[i]);
                Long accountId = Long.parseLong(transactionsTemplate[i+1]);
                Account account = accountsList.stream().filter(a->a.getId().equals(accountId)).findFirst().get();
                Double amount = Double.parseDouble(transactionsTemplate[i+2]);
                Date effectiveDate = dateFormat.parse(transactionsTemplate[i+3]);
                String transactionName = transactionsTemplate[i+4];

                // create transaction
                Transaction transaction = new Transaction();

                // set transaction properties
                Field idField = transaction.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(transaction, transactionId);
                idField.setAccessible(false);
                transaction.setAccount(account);
                transaction.setAmount(amount);
                transaction.setEffectiveDate(effectiveDate);
                transaction.setDescription(transactionName);
                transaction.getAccount().getTransactions().add(transaction); // add the Transaction to the Account.

                transactionList.add(transaction);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setUp() {
        createAccounts();
        createTransactions();
    }

    @Test
    public void balance() {
        Account a1 = accountsList.stream().filter(a -> a.getId() == 1).findFirst().get();
        assertEquals(new Double(22.39d), a1.getBalance());
    }
}
