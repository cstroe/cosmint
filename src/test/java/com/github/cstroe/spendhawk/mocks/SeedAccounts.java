package com.github.cstroe.spendhawk.mocks;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SeedAccounts {

    private User mockUser;
    private Account incomeAccount;
    private Account expenseAccount;
    private Account assetAccount;
    private Account myBankAccount;

    public SeedAccounts() {
        Integer accountIdSeq = 1;

        mockUser = new User(13L, "MockUser", Collections.emptyList());

        incomeAccount = new Account();
        incomeAccount.setId(++accountIdSeq);
        incomeAccount.setName(AccountService.DEFAULT_INCOME_ACCOUNT_NAME);
        incomeAccount.setUser(mockUser);
        incomeAccount.setTransactions(new LinkedList<>());

        expenseAccount = new Account();
        expenseAccount.setId(++accountIdSeq);
        expenseAccount.setName(AccountService.DEFAULT_EXPENSE_ACCOUNT_NAME);
        expenseAccount.setUser(mockUser);
        expenseAccount.setTransactions(new LinkedList<>());

        assetAccount = new Account();
        assetAccount.setId(++accountIdSeq);
        assetAccount.setName(AccountService.DEFAULT_ASSET_ACCOUNT_NAME);
        assetAccount.setUser(mockUser);
        assetAccount.setTransactions(new LinkedList<>());

        myBankAccount = new Account();
        myBankAccount.setId(++accountIdSeq);
        myBankAccount.setName("My Bank Account");
        myBankAccount.setUser(mockUser);
        myBankAccount.setTransactions(new LinkedList<>());

        List<Account> accounts = new ArrayList<>(4);
        accounts.add(incomeAccount);
        accounts.add(expenseAccount);
        accounts.add(assetAccount);
        accounts.add(myBankAccount);
        mockUser.setAccounts(accounts);
    }

    public User getMockUser() {
        return mockUser;
    }

    public Account getIncomeAccount() {
        return incomeAccount;
    }

    public Account getExpenseAccount() {
        return expenseAccount;
    }

    public Account getAssetAccount() {
        return assetAccount;
    }

    public Account getMyBankAccount() {
        return myBankAccount;
    }
}
