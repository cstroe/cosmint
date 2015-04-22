package com.github.cstroe.spendhawk.entity;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

public class AccountTest {

    private EntityTestScenario scenario;

    @Before
    public void setUp() {
        String[] accountsTemplate = new String[] {
                // Account ID, Account Name
                "1", "Account1"
        };
        String[] transactionsTemplate = new String[] {
                // Transaction ID, Account ID, Amount, Date ("yyMMddHHmmss"), Description
                "1", "1", "10.99", "010101000000", "Transaction 1",
                "1", "1", "11.40", "010101000000", "Transaction 2"
        };
        scenario = new EntityTestScenario(accountsTemplate, transactionsTemplate);
    }

    @Test
    public void balance() {
        Account a1 = scenario.getAccountsList().stream().filter(a -> a.getId() == 1).findFirst().get();
        assertEquals(new Double(22.39d), a1.getBalance());
        assertEquals(2, a1.getTransactions().size());
    }

    @Test
    public void balanceWithoutAnyTransactions() {
        Account a1 = new Account();
        a1.setName("Test Account");
        assertEquals(new Double(0d), a1.getBalance());
    }

    @Test
    public void compare() {
        char c = 'z';

        List<Account> accountList = new ArrayList<>();
        for (long i = 0; i <= 'z' - 'a'; i++) {
            Account currentAccount = new Account();
            currentAccount.setId(i);
            currentAccount.setName("Account " + Character.toString((char) (c - i)));
            accountList.add(currentAccount);
        }

        Collections.shuffle(accountList);
        Collections.sort(accountList);

        for (int i = 0; i < accountList.size() - 1; i++) {
            Account currentAccount = accountList.get(i);
            Account nextAccount = accountList.get(i + 1);
            assertThat("A current account should be before the next account",
                    currentAccount.compareTo(nextAccount), is(-1));
            assertThat("A next account should be after the current account",
                    nextAccount.compareTo(currentAccount), is(1));
        }
    }

    @Test
    public void getDepth() {
        Account p = new Account();
        assertThat(p.getDepth(), is(0));

        Account c1 = new Account();
        c1.setParent(p);

        assertThat(c1.getDepth(), is(1));

        Account c2 = new Account();
        c2.setParent(c1);

        assertThat(c2.getDepth(), is(2));
    }
}
