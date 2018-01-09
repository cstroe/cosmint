package com.github.cstroe.spendhawk.dao;

import com.github.cstroe.spendhawk.util.Ex;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EntityTestScenario {

    private List<AccountDao> accountsList = new ArrayList<>();
    private List<TransactionDao> transactionList = new ArrayList<>();

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
            for (int i = 0; i < accountsTemplate.length; i += 3) {
                Long accountId = Long.parseLong(accountsTemplate[i]);
                String accountName = accountsTemplate[i+1];
                String parentAccountIdRaw = accountsTemplate[i+2];

                // create account
                AccountDao account = new AccountDao();

                // set account properties
                Field idField = account.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(account, accountId);
                idField.setAccessible(false);
                account.setName(accountName);
//                account.setTransactions(new ArrayList<>());
                if(parentAccountIdRaw != null) {
                    final Long parentAccountId = Long.parseLong(parentAccountIdRaw);
                    AccountDao parentAccount = accountsList.stream()
                        .filter(a -> a.getId().equals(parentAccountId))
                        .findFirst().orElseThrow(Ex::accountNotFound);
//                    account.setParent(parentAccount);
                }
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
                AccountDao account = accountsList.stream().filter(a->a.getId().equals(accountId)).findFirst().get();
                Double amount = Double.parseDouble(transactionsTemplate[i+2]);
                Date effectiveDate = dateFormat.parse(transactionsTemplate[i+3]);
                String transactionName = transactionsTemplate[i+4];

                // create transaction
                TransactionDao transaction = new TransactionDao();

                // set transaction properties
                Field idField = transaction.getClass().getDeclaredField("id");
                idField.setAccessible(true);
                idField.set(transaction, transactionId);
                idField.setAccessible(false);
//                transaction.setAccount(account);
//                transaction.setAmount(amount);
//                transaction.setEffectiveDate(effectiveDate);
//                transaction.setDescription(transactionName);
//                transaction.getAccount().getTransactions().add(transaction); // add the TransactionDao to the AccountDao.

                transactionList.add(transaction);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public List<AccountDao> getAccountsList() {
        return accountsList;
    }
}
