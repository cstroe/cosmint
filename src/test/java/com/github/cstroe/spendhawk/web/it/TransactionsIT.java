package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.CashFlow;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Ex;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Integration tests having to do with transactions.
 */
@RunWith(Arquillian.class)
public class TransactionsIT extends BaseIT {

    @Test
    public void seedDatabaseWorks() {
        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Ex::userNotFound);
        List<Account> accounts = Account.findAll(currentUser);
        assertEquals(3, accounts.size());

        Collections.sort(accounts);

        assertEquals("Asset", accounts.get(0).getName());
        assertEquals("Expense", accounts.get(1).getName());
        assertEquals("Income", accounts.get(2).getName());
        commitTransaction();
    }

    @Test
    public void seedDatabaseWorks2() {
        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Ex::userNotFound);
        List<Account> accounts = Account.findAll(currentUser);
        assertEquals(3, accounts.size());

        Collections.sort(accounts);

        assertEquals("Asset", accounts.get(0).getName());
        assertEquals("Expense", accounts.get(1).getName());
        assertEquals("Income", accounts.get(2).getName());
        commitTransaction();
    }

    @Test
    public void find_cashflows() {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Ex::userNotFound);
        Account userAccount = Account.findById(currentUser, 17l).orElseThrow(Ex::accountNotFound);

        List<CashFlow> cashFlows = userAccount.findCashFlows("Park");
        assertThat(cashFlows.size(), is(1));
    }
}
