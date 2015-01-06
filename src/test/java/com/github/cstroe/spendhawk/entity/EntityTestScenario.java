package com.github.cstroe.spendhawk.entity;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityTestScenario {

    private List<Account> accountsList = new ArrayList<>();
    private List<Transaction> transactionList = new ArrayList<>();

    protected final String[] accountsTemplate;
    protected final String[] transactionsTemplate;

    public EntityTestScenario(String[] accountsTemplate, String[] transactionsTemplate) {
        this.accountsTemplate = accountsTemplate;
        this.transactionsTemplate = transactionsTemplate;
        createAccounts();
        createTransactions();
    }

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

    public List<Account> getAccountsList() {
        return accountsList;
    }
}
