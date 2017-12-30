package com.github.cstroe.spendhawk.mocks;

import com.github.cstroe.spendhawk.bean.AccountService;
import com.github.cstroe.spendhawk.dao.AccountDao;
import com.github.cstroe.spendhawk.dao.UserDao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeedAccounts {

    private UserDao mockUser;
    private AccountDao incomeAccount;
    private AccountDao expenseAccount;
    private AccountDao assetAccount;
    private AccountDao myBankAccount;

    public SeedAccounts() {
        Long accountIdSeq = 1L;

        mockUser = new UserDao(13L, "MockUser", Collections.emptyList());

        incomeAccount = new AccountDao();
        incomeAccount.setId(++accountIdSeq);
        incomeAccount.setName(AccountService.DEFAULT_INCOME_ACCOUNT_NAME);
        incomeAccount.setUser(mockUser);
//        incomeAccount.setTransactions(new LinkedList<>());

        expenseAccount = new AccountDao();
        expenseAccount.setId(++accountIdSeq);
        expenseAccount.setName(AccountService.DEFAULT_EXPENSE_ACCOUNT_NAME);
        expenseAccount.setUser(mockUser);
//        expenseAccount.setTransactions(new LinkedList<>());

        assetAccount = new AccountDao();
        assetAccount.setId(++accountIdSeq);
        assetAccount.setName(AccountService.DEFAULT_ASSET_ACCOUNT_NAME);
        assetAccount.setUser(mockUser);
//        assetAccount.setTransactions(new LinkedList<>());

        myBankAccount = new AccountDao();
        myBankAccount.setId(++accountIdSeq);
        myBankAccount.setName("My Bank AccountDao");
        myBankAccount.setUser(mockUser);
//        myBankAccount.setTransactions(new LinkedList<>());

        List<AccountDao> accounts = new ArrayList<>(4);
        accounts.add(incomeAccount);
        accounts.add(expenseAccount);
        accounts.add(assetAccount);
        accounts.add(myBankAccount);
        mockUser.setAccounts(accounts);
    }

    public UserDao getMockUser() {
        return mockUser;
    }

    public AccountDao getIncomeAccount() {
        return incomeAccount;
    }

    public AccountDao getExpenseAccount() {
        return expenseAccount;
    }

    public AccountDao getAssetAccount() {
        return assetAccount;
    }

    public AccountDao getMyBankAccount() {
        return myBankAccount;
    }
}
