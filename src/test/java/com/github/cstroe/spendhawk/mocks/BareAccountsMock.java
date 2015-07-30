package com.github.cstroe.spendhawk.mocks;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;

import java.util.ArrayList;
import java.util.List;

public class BareAccountsMock {

    private User mockUser;
    private Account incomeAccount;
    private Account expenseAccount;
    private Account assetAccount;
    private Account myBankAccount;

    public BareAccountsMock() {
        Long accountIdSeq = 1l;

        mockUser = new User();
        mockUser.setId(13l);
        mockUser.setName("MockUser");

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
