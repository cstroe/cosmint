package com.github.cstroe.spendhawk.mocks;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BareAccountsMock {

    private User mockUser;
    private Account incomeAccount;
    private Account expenseAccount;
    private Account assetAccount;
    private Account myBankAccount;

    public BareAccountsMock() {
        Integer accountIdSeq = 1;

        mockUser = new User();
        mockUser.setId(13);
        mockUser.setName("MockUser");

        incomeAccount = new Account();
        incomeAccount.setId(++accountIdSeq);
        incomeAccount.setName(AccountService.DEFAULT_INCOME_ACCOUNT_NAME);
//        incomeAccount.setParent(null);
        incomeAccount.setUser(mockUser);
//        incomeAccount.setCashFlows(new LinkedList<>());

        expenseAccount = new Account();
        expenseAccount.setId(++accountIdSeq);
        expenseAccount.setName(AccountService.DEFAULT_EXPENSE_ACCOUNT_NAME);
//        expenseAccount.setParent(null);
        expenseAccount.setUser(mockUser);
//        expenseAccount.setCashFlows(new LinkedList<>());

        assetAccount = new Account();
        assetAccount.setId(++accountIdSeq);
        assetAccount.setName(AccountService.DEFAULT_ASSET_ACCOUNT_NAME);
//        assetAccount.setParent(null);
        assetAccount.setUser(mockUser);
//        assetAccount.setCashFlows(new LinkedList<>());

        myBankAccount = new Account();
        myBankAccount.setId(++accountIdSeq);
        myBankAccount.setName("My Bank Account");
//        myBankAccount.setParent(assetAccount);
        myBankAccount.setUser(mockUser);
//        myBankAccount.setCashFlows(new LinkedList<>());

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
