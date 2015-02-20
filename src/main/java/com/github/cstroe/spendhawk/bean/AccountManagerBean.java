package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.Transaction;

import javax.ejb.Stateless;
import java.util.Optional;

@Stateless
public class AccountManagerBean {

    private String message;

    public String getMessage() {
        return message;
    }

    public Optional<Account> createAccount(Long userId, String accountName) {
        Transaction currentTransaction = null;
        try {
            if(!HibernateUtil.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
                currentTransaction = HibernateUtil.getSessionFactory().getCurrentSession().beginTransaction();
            }

            User currentUser = User.findById(userId);

            Account theAccount = new Account();
            theAccount.setName(accountName);
            theAccount.setUser(currentUser);

            HibernateUtil.getSessionFactory().getCurrentSession().save(theAccount);
            if(currentTransaction != null) {
                currentTransaction.commit();
            }

            return Optional.of(theAccount);
        } catch(Exception ex) {
            if(currentTransaction != null) {
                currentTransaction.rollback();
            }

            message = ex.getMessage();
            return Optional.empty();
        }
    }
}