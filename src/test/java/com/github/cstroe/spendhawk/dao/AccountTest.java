package com.github.cstroe.spendhawk.dao;

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
                // AccountDao ID, AccountDao Name, Parent AccountDao
                "1", "Account1", null
        };
        String[] transactionsTemplate = new String[] {
                // TransactionDao ID, AccountDao ID, Amount, Date ("yyMMddHHmmss"), Description
                "1", "1", "10.99", "010101000000", "TransactionDao 1",
                "1", "1", "11.40", "010101000000", "TransactionDao 2"
        };
        EntityTestScenario scenario = new EntityTestScenario(accountsTemplate, transactionsTemplate);

        AccountDao a1 = scenario.getAccountsList().stream().filter(a -> a.getId() == 1).findFirst().get();
//        assertEquals(new Double(22.39d), a1.getBalance());
        //assertEquals(2, a1.getTransactions().size());
    }

    @Test
    public void balanceWithoutAnyTransactions() {
        AccountDao a1 = new AccountDao();
        a1.setName("Test AccountDao");
//        assertEquals(new Double(0d), a1.getBalance());
    }

    @Test
    public void compare() {
        char c = 'z';

        List<AccountDao> accountList = new ArrayList<>();
        for (long i = 0; i <= 'z' - 'a'; i++) {
            AccountDao currentAccount = new AccountDao();
            currentAccount.setId(i);
            currentAccount.setName("AccountDao " + Character.toString((char) (c - i)));
            accountList.add(currentAccount);
        }

        Collections.shuffle(accountList);
        Collections.sort(accountList);

        for (int i = 0; i < accountList.size() - 1; i++) {
            AccountDao currentAccount = accountList.get(i);
            AccountDao nextAccount = accountList.get(i + 1);
            assertThat("A current account should be before the next account",
                    currentAccount.compareTo(nextAccount), is(-1));
            assertThat("A next account should be after the current account",
                    nextAccount.compareTo(currentAccount), is(1));
        }
    }

    @Test
    public void getDepth() {
        AccountDao p = new AccountDao();
//        assertThat("Top level depth should be 0.", p.getDepth(), is(0));
//
//        AccountDao c1 = new AccountDao().withParent(p);
//
//        assertThat("Depth should be incremented by 1.", c1.getDepth(), is(1));
//
//        AccountDao c2 = new AccountDao();
//        c2.setParent(c1);
//
//        assertThat("Depth should work for deeper sub-accounts.", c2.getDepth(), is(2));
    }

    @Test
    public void getPath() {
//        AccountDao p = new AccountDao().withName("AccountDao 1");
//        AccountDao c1 = new AccountDao().withName("Child 1").andParent(p);
//        AccountDao c2 = new AccountDao().withName("Child 2").andParent(c1);
//
//        assertThat(c2.getPath(), is(equalTo("AccountDao 1Child 1Child 2")));
    }

    @Test
    public void sort_top_level_accounts() {
//        List<AccountDao> accounts = new ArrayList<>();
//        accounts.add(new AccountDao().withName("AccountDao A")); // 0
//        accounts.add(new AccountDao().withName("AccountDao B"));
//        accounts.add(new AccountDao().withName("AccountDao C"));
//        accounts.add(new AccountDao().withName("AccountDao D"));
//        accounts.add(new AccountDao().withName("AccountDao E")); //4
//        accounts.add(new AccountDao().withName("AccountDao F"));
//        accounts.add(new AccountDao().withName("AccountDao F"));
//        accounts.add(new AccountDao().withName("AccountDao G"));
//        accounts.add(new AccountDao().withName("AccountDao H"));
//        accounts.add(new AccountDao().withName("AccountDao H")); // 9
//
//        Collections.shuffle(accounts);
//
//        Collections.sort(accounts, AccountDao.HIERARCHICAL_COMPARATOR);
//
//        Assert.assertThat(accounts.get(0).getName(), is(equalTo("AccountDao A")));
//        Assert.assertThat(accounts.get(1).getName(), is(equalTo("AccountDao B")));
//        Assert.assertThat(accounts.get(2).getName(), is(equalTo("AccountDao C")));
//        Assert.assertThat(accounts.get(3).getName(), is(equalTo("AccountDao D")));
//        Assert.assertThat(accounts.get(4).getName(), is(equalTo("AccountDao E")));
//        Assert.assertThat(accounts.get(5).getName(), is(equalTo("AccountDao F")));
//        Assert.assertThat(accounts.get(6).getName(), is(equalTo("AccountDao F")));
//        Assert.assertThat(accounts.get(7).getName(), is(equalTo("AccountDao G")));
//        Assert.assertThat(accounts.get(8).getName(), is(equalTo("AccountDao H")));
//        Assert.assertThat(accounts.get(9).getName(), is(equalTo("AccountDao H")));
    }

    @Test
    public void sort_one_top_level_account_and_children() {
/*
        AccountDao p = new AccountDao().withName("Parent AccountDao");
        List<AccountDao> accounts = new ArrayList<>();
        accounts.add(p);
        accounts.add(new AccountDao().withName("Child A").andParent(p));
        accounts.add(new AccountDao().withName("Child B").andParent(p));
        accounts.add(new AccountDao().withName("Child C").andParent(p));
        accounts.add(new AccountDao().withName("Child D").andParent(p));
        accounts.add(new AccountDao().withName("Child E").andParent(p));
        accounts.add(new AccountDao().withName("Child E").andParent(p));
        accounts.add(new AccountDao().withName("Child F").andParent(p));
        accounts.add(new AccountDao().withName("Child G").andParent(p));

        Collections.shuffle(accounts);

        Collections.sort(accounts, (AccountDao a, AccountDao b) -> a.getPath().compareTo(b.getPath()));

        Assert.assertThat(accounts.get(0).getName(), is(equalTo("Parent AccountDao")));
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
        AccountDao p1 = new AccountDao().withName("Parent AccountDao 1");
        AccountDao p2 = new AccountDao().withName("Parent AccountDao 2");
        List<AccountDao> accounts = new ArrayList<>();
        accounts.add(p1);
        accounts.add(p2);
        accounts.add(new AccountDao().withName("Child A").andParent(p1));
        accounts.add(new AccountDao().withName("Child B").andParent(p2));
        accounts.add(new AccountDao().withName("Child C").andParent(p1));
        accounts.add(new AccountDao().withName("Child D").andParent(p2));
        accounts.add(new AccountDao().withName("Child E").andParent(p1));
        accounts.add(new AccountDao().withName("Child E").andParent(p2));
        accounts.add(new AccountDao().withName("Child F").andParent(p1));
        accounts.add(new AccountDao().withName("Child G").andParent(p2));

        Collections.shuffle(accounts);

        Collections.sort(accounts, AccountDao.HIERARCHICAL_COMPARATOR);

        Assert.assertThat(accounts.get(0).getName(), is(equalTo("Parent AccountDao 1")));
        Assert.assertThat(accounts.get(1).getName(), is(equalTo("Child A")));
        Assert.assertThat(accounts.get(2).getName(), is(equalTo("Child C")));
        Assert.assertThat(accounts.get(3).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(4).getName(), is(equalTo("Child F")));
        Assert.assertThat(accounts.get(5).getName(), is(equalTo("Parent AccountDao 2")));
        Assert.assertThat(accounts.get(6).getName(), is(equalTo("Child B")));
        Assert.assertThat(accounts.get(7).getName(), is(equalTo("Child D")));
        Assert.assertThat(accounts.get(8).getName(), is(equalTo("Child E")));
        Assert.assertThat(accounts.get(9).getName(), is(equalTo("Child G")));
*/
    }

    @Test
    public void sort_two_deep_accounts() {
/*
        AccountDao a = new AccountDao().withName("a");
        AccountDao b = new AccountDao().withName("b").andParent(a);
        AccountDao c = new AccountDao().withName("c").andParent(b);
        AccountDao d = new AccountDao().withName("d").andParent(c);
        AccountDao e = new AccountDao().withName("e").andParent(d);

        AccountDao v = new AccountDao().withName("v");
        AccountDao w = new AccountDao().withName("w").andParent(v);
        AccountDao x = new AccountDao().withName("x").andParent(w);
        AccountDao y = new AccountDao().withName("y").andParent(x);
        AccountDao z = new AccountDao().withName("z").andParent(y);

        List<AccountDao> accounts = new ArrayList<>();
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

        Collections.sort(accounts, AccountDao.HIERARCHICAL_COMPARATOR);

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
