package com.github.cstroe.spendhawk.cli;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.Session;

import java.util.Arrays;
import java.util.List;

public class AccountManager {
    public static void main(String[] args) {
        AccountManager accountManager = new AccountManager();
        if("create".equals(args[0])) {
            String accountName = Arrays.stream(Arrays.copyOfRange(args, 1, args.length))
                    .reduce("", (a, b) -> a.concat(" ").concat(b));
            Account account = accountManager.createAccount(accountName);
            System.out.println("Created new account with id: " + account.getId());
        } else if("list".endsWith(args[0])) {
            List<Account> accounts = accountManager.listAccounts();
            accounts.stream().forEach(a ->
                System.out.println("Account Id: " + a.getId() + ", " + a.getName()
            ));
        }

        HibernateUtil.closeSessionFactory();
    }

    private Account createAccount(String accountName) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Account account = new Account();
        account.setName(accountName);

        session.save(account);
        session.getTransaction().commit();

        return account;
    }

    @SuppressWarnings("unchecked")
    private List<Account> listAccounts() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Account").list();
        session.getTransaction().commit();
        return result;
    }
}
