package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Optional;
import java.util.Set;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class AccountManagerBeanIT extends BaseIT {

    @Inject
    private AccountManagerBean accountManager;

    @Test
    public void testCreateAccount() {
        String accountName = "Test Created Account";
        Optional<Account> account = accountManager.createAccount(1l, accountName);

        assertTrue("Account should be created.", account.isPresent());

        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Optional<Account> maybeRetrieved = Account.findById(currentUser, account.get().getId());

        assertTrue("Account should be persisted.", maybeRetrieved.isPresent());

        Account retrieved = maybeRetrieved.get();
        assertEquals("Account name should be correctly persisted.", accountName, retrieved.getName());
        assertEquals("Account user should be correctly persisted.", 1l, (long)retrieved.getUser().getId());
//        assertEquals("Empty account should have 0 transactions.", 0, retrieved.getTransactions().size());
        assertEquals("Empty account should have a 0 balance.", 0d, retrieved.getBalance(), 0.0001);
        commitTransaction();
    }

    @Test
    public void testCreateAccountWithParent() {
        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Account parent = Account.findById(currentUser, 1l)
            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));

        Optional<Account> newAccountOptional =
            accountManager.createAccount(1l, "New Account", Optional.of(parent.getId()));

        assertTrue("Account should be created.", newAccountOptional.isPresent());

        Account newAccount = newAccountOptional.get();

        assertThat(newAccount.getParent(), is(equalTo(parent)));
        commitTransaction();
    }

    @Test
    public void testSeedSubAccount() {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Exceptions::userNotFound);
        Account parent = Account.findById(currentUser, 2l)
            .orElseThrow(() -> new AssertionError("Account 2 should exist in the seed data."));

        assertThat(parent.getSubAccounts().size(), is(1));
        assertThat(parent.getSubAccounts().iterator().next().getName(),
            is(equalTo("General Spending Account")));
    }

    @Test
    public void testSubAccounts() {
        final String account1name = "Sub Account 1";
        final String account2name = "Sub Account 2";

        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Account parent = Account.findById(currentUser, 1l)
            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));

        Optional<Account> newAccountOptional1 =
            accountManager.createAccount(1l, account1name, Optional.of(parent.getId()));

        assertTrue("Account should be created.", newAccountOptional1.isPresent());

        Optional<Account> newAccountOptional2 =
                accountManager.createAccount(1l, account2name, Optional.of(parent.getId()));

        assertTrue("Account should be created.", newAccountOptional2.isPresent());

        commitTransaction();
        startTransaction();

        parent = Account.findById(currentUser, 1l)
            .orElseThrow(() -> new AssertionError("Account 1 should exist in the seed data."));

        assertThat(parent.getSubAccounts().size(), is(2));

        Set<Account> children = parent.getSubAccounts();

        assertTrue(account1name + " should be presisted.",
            children.stream().filter(a -> a.getName().equals(account1name)).findFirst().isPresent());

        assertTrue(account2name + " should be persisted.",
            children.stream().filter(a -> a.getName().equals(account2name)).findFirst().isPresent());

        commitTransaction();
    }

    @Test
    public void testNestAccounts() {
        startTransaction();
        User currentUser = User.findById(3l).orElseThrow(Exceptions::userNotFound);

        Account subAccount = Account.findById(currentUser, 2l)
            .orElseThrow(Exceptions::accountNotFound);

        assertNull("The sub account should not have a parent.", subAccount.getParent());

        accountManager.nestAccount(currentUser.getId(), 4l, 2l);
        commitTransaction();

        startTransaction();

        Account parentAccount = Account.findById(currentUser, 4l).get();
        subAccount = Account.findById(currentUser, 2l).get();

        assertThat(subAccount.getParent(), is(equalTo(parentAccount)));
        commitTransaction();
    }

    @Test
    public void testCreateAccountWithBlankName() {
        String accountName = "";
        Optional<Account> account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with blank name should not be created.", account.isPresent());
        assertEquals("There should be a message when an account is not created.",
            "Account name cannot be blank.", accountManager.getMessage());

        accountName = "   ";
        account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with only spaces in name should not be created.",
            account.isPresent());

        accountName = "\t\t\t";
        account = accountManager.createAccount(1l, accountName);
        assertFalse("Account with only tabs in name should not be created.",
            account.isPresent());
    }

    @Test
    public void testHTMLCodeInAccountName() {
        String accountName = "</a><a href=\"bad_place.com\">";
        Account account = accountManager.createAccount(1l, accountName).get();
        assertNotEquals("HTML should not show up in the account name.",
            account.getName(), accountName);
    }

    @Test
    public void testSQLInjectionInAccountName() {
        String accountName = "' or 1";
        Account account = accountManager.createAccount(1l, accountName).get();
        assertFalse("HTML should not show up in the account name.",
                account.getName().contains("'"));
    }

    @Test
    public void testDeleteAccount() {
        if(!accountManager.deleteAccount(1l, 1l)) {
            fail("Should be able to delete an account. " + accountManager.getMessage());
        }

        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Account.findById(currentUser, 1l).ifPresent(a -> fail("Account deletion should be persisted."));
        Transaction.findById(1l).ifPresent(t -> fail("Transaction in the account should be deleted."));
//        Expense.findById(1l).ifPresent(t -> fail("Expenses in the transactions of the account should be deleted."));
        commitTransaction();
    }
}
