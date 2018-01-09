package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.dao.UserDao;
import org.junit.Test;

/**
 * Integration tests having to do with transactions.
 */
public class TransactionsIT {

    @Test
    public void seedDatabaseWorks() {
        UserDao currentUser = null; //UserDao.findById(1).orElseThrow(Ex::userNotFound);
//        List<AccountDao> accounts = AccountDao.findAll(currentUser);
//        assertEquals(3, accounts.size());

//        Collections.sort(accounts);

//        assertEquals("Asset", accounts.get(0).getName());
//        assertEquals("Expense", accounts.get(1).getName());
//        assertEquals("Income", accounts.get(2).getName());
    }

    @Test
    public void seedDatabaseWorks2() {
        UserDao currentUser = null; //UserDao.findById(1L).orElseThrow(Ex::userNotFound);
//        List<AccountDao> accounts = AccountDao.findAll(currentUser);
//        assertEquals(3, accounts.size());
//
//        Collections.sort(accounts);
//
//        assertEquals("Asset", accounts.get(0).getName());
//        assertEquals("Expense", accounts.get(1).getName());
//        assertEquals("Income", accounts.get(2).getName());
    }

    @Test
    public void find_cashflows() {
//        UserDao currentUser = null; //UserDao.findById(3L).orElseThrow(Ex::userNotFound);
//        AccountDao userAccount = AccountDao.findById(currentUser, 17).orElseThrow(Ex::accountNotFound);
//
//        List<CashFlow> cashFlows = userAccount.findCashFlows("Park");
//        assertThat(cashFlows.size(), is(1));
    }
}
