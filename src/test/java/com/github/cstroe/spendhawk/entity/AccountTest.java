package com.github.cstroe.spendhawk.entity;

import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AccountTest {

    @Test
    @Ignore
    public void balance() {
        String[] accountsTemplate = new String[] {
                // Account ID, Account Name, Parent Account
                "1", "Account1", null
        };
        String[] transactionsTemplate = new String[] {
                // Transaction ID, Account ID, Amount, Date ("yyMMddHHmmss"), Description
                "1", "1", "10.99", "010101000000", "Transaction 1",
                "1", "1", "11.40", "010101000000", "Transaction 2"
        };
        EntityTestScenario scenario = new EntityTestScenario(accountsTemplate, transactionsTemplate);

        Account a1 = scenario.getAccountsList().stream().filter(a -> a.getId() == 1).findFirst().get();
//        assertEquals(new Double(22.39d), a1.getBalance());
        //assertEquals(2, a1.getTransactions().size());
    }

    @Test
    public void balanceWithoutAnyTransactions() {
        Account a1 = new Account();
        a1.setName("Test Account");
//        assertEquals(new Double(0d), a1.getBalance());
    }

    @Test
    public void compare() {
        char c = 'z';

        List<Account> accountList = new ArrayList<>();
        for (int i = 0; i <= 'z' - 'a'; i++) {
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
//        assertThat("Top level depth should be 0.", p.getDepth(), is(0));
//
//        Account c1 = new Account().withParent(p);
//
//        assertThat("Depth should be incremented by 1.", c1.getDepth(), is(1));
//
//        Account c2 = new Account();
//        c2.setParent(c1);
//
//        assertThat("Depth should work for deeper sub-accounts.", c2.getDepth(), is(2));
    }

    @Test
    public void getPath() {
//        Account p = new Account().withName("Account 1");
//        Account c1 = new Account().withName("Child 1").andParent(p);
//        Account c2 = new Account().withName("Child 2").andParent(c1);
//
//        assertThat(c2.getPath(), is(equalTo("Account 1Child 1Child 2")));
    }

    @Test
    public void sort_top_level_accounts() {
//        List<Account> accounts = new ArrayList<>();
//        accounts.add(new Account().withName("Account A")); // 0
//        accounts.add(new Account().withName("Account B"));
//        accounts.add(new Account().withName("Account C"));
//        accounts.add(new Account().withName("Account D"));
//        accounts.add(new Account().withName("Account E")); //4
//        accounts.add(new Account().withName("Account F"));
//        accounts.add(new Account().withName("Account F"));
//        accounts.add(new Account().withName("Account G"));
//        accounts.add(new Account().withName("Account H"));
//        accounts.add(new Account().withName("Account H")); // 9
//
//        Collections.shuffle(accounts);
//
//        Collections.sort(accounts, Account.HIERARCHICAL_COMPARATOR);
//
//        Assert.assertThat(accounts.get(0).getName(), is(equalTo("Account A")));
//        Assert.assertThat(accounts.get(1).getName(), is(equalTo("Account B")));
//        Assert.assertThat(accounts.get(2).getName(), is(equalTo("Account C")));
//        Assert.assertThat(accounts.get(3).getName(), is(equalTo("Account D")));
//        Assert.assertThat(accounts.get(4).getName(), is(equalTo("Account E")));
//        Assert.assertThat(accounts.get(5).getName(), is(equalTo("Account F")));
//        Assert.assertThat(accounts.get(6).getName(), is(equalTo("Account F")));
//        Assert.assertThat(accounts.get(7).getName(), is(equalTo("Account G")));
//        Assert.assertThat(accounts.get(8).getName(), is(equalTo("Account H")));
//        Assert.assertThat(accounts.get(9).getName(), is(equalTo("Account H")));
    }

    @Test
    public void sort_one_top_level_account_and_children() {
/*
        Account p = new Account().withName("Parent Account");
        List<Account> accounts = new ArrayList<>();
        accounts.add(p);
        accounts.add(new Account().withName("Child A").andParent(p));
        accounts.add(new Account().withName("Child B").andParent(p));
        accounts.add(new Account().withName("Child C").andParent(p));
        accounts.add(new Account().withName("Child D").andParent(p));
        accounts.add(new Account().withName("Child E").andParent(p));
        accounts.add(new Account().withName("Child E").andParent(p));
        accounts.add(new Account().withName("Child F").andParent(p));
        accounts.add(new Account().withName("Child G").andParent(p));

        Collections.shuffle(accounts);

        Collections.sort(accounts, (Account a, Account b) -> a.getPath().compareTo(b.getPath()));

        Assert.assertThat(accounts.get(0).getName(), is(equalTo("Parent Account")));
        Assert.assertThat(accounts.get(1).getName(), is(equalTo("Child A")));
        Assert.assertThat(accounts.get(2).getName(), is(equalTo("Child B")));
        Assert.assertThat(accounts.get(3).getName(), is(equalTo("Child C")));
        Assert.assertThat(accounts.get(4).getName(), is(equalTo("Child D")));
        Assert.assertThat(accounts.get(5).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(6).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(7).getName(), is(equalTo("Child F")));
        Assert.assertThat(accounts.get(8).getName(), is(equalTo("Child G")));
*/
    }

    @Test
    public void sort_two_top_level_accounts_and_children() {
/*
        Account p1 = new Account().withName("Parent Account 1");
        Account p2 = new Account().withName("Parent Account 2");
        List<Account> accounts = new ArrayList<>();
        accounts.add(p1);
        accounts.add(p2);
        accounts.add(new Account().withName("Child A").andParent(p1));
        accounts.add(new Account().withName("Child B").andParent(p2));
        accounts.add(new Account().withName("Child C").andParent(p1));
        accounts.add(new Account().withName("Child D").andParent(p2));
        accounts.add(new Account().withName("Child E").andParent(p1));
        accounts.add(new Account().withName("Child E").andParent(p2));
        accounts.add(new Account().withName("Child F").andParent(p1));
        accounts.add(new Account().withName("Child G").andParent(p2));

        Collections.shuffle(accounts);

        Collections.sort(accounts, Account.HIERARCHICAL_COMPARATOR);

        Assert.assertThat(accounts.get(0).getName(), is(equalTo("Parent Account 1")));
        Assert.assertThat(accounts.get(1).getName(), is(equalTo("Child A")));
        Assert.assertThat(accounts.get(2).getName(), is(equalTo("Child C")));
        Assert.assertThat(accounts.get(3).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(4).getName(), is(equalTo("Child F")));
        Assert.assertThat(accounts.get(5).getName(), is(equalTo("Parent Account 2")));
        Assert.assertThat(accounts.get(6).getName(), is(equalTo("Child B")));
        Assert.assertThat(accounts.get(7).getName(), is(equalTo("Child D")));
        Assert.assertThat(accounts.get(8).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(9).getName(), is(equalTo("Child G")));
*/
    }

    @Test
    public void sort_two_deep_accounts() {
/*
        Account a = new Account().withName("a");
        Account b = new Account().withName("b").andParent(a);
        Account c = new Account().withName("c").andParent(b);
        Account d = new Account().withName("d").andParent(c);
        Account e = new Account().withName("e").andParent(d);

        Account v = new Account().withName("v");
        Account w = new Account().withName("w").andParent(v);
        Account x = new Account().withName("x").andParent(w);
        Account y = new Account().withName("y").andParent(x);
        Account z = new Account().withName("z").andParent(y);

        List<Account> accounts = new ArrayList<>();
        accounts.add(a);
        accounts.add(b);
        accounts.add(c);
        accounts.add(d);
        accounts.add(e);
        accounts.add(v);
        accounts.add(w);
        accounts.add(x);
        accounts.add(z);
        accounts.add(y);

        Collections.shuffle(accounts);

        Collections.sort(accounts, Account.HIERARCHICAL_COMPARATOR);

        Assert.assertThat(accounts.get(0).getName(), is(equalTo(a.getName())));
        Assert.assertThat(accounts.get(1).getName(), is(equalTo(b.getName())));
        Assert.assertThat(accounts.get(2).getName(), is(equalTo(c.getName())));
        Assert.assertThat(accounts.get(3).getName(), is(equalTo(d.getName())));
        Assert.assertThat(accounts.get(4).getName(), is(equalTo(e.getName())));
        Assert.assertThat(accounts.get(5).getName(), is(equalTo(v.getName())));
        Assert.assertThat(accounts.get(6).getName(), is(equalTo(w.getName())));
        Assert.assertThat(accounts.get(7).getName(), is(equalTo(x.getName())));
        Assert.assertThat(accounts.get(8).getName(), is(equalTo(y.getName())));
        Assert.assertThat(accounts.get(9).getName(), is(equalTo(z.getName())));
*/
    }
}
