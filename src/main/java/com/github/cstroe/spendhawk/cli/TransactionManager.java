package com.github.cstroe.spendhawk.cli;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.hibernate.Session;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TransactionManager {
    public static void main(String[] args) throws ParseException {
        TransactionManager manager = new TransactionManager();
        if("create".equals(args[0])) {
            Long accountId = Long.parseLong(args[1]);
            Double amount = Double.parseDouble(args[2]);
            Date effectiveDate = new SimpleDateFormat("yyMMddHHmmss").parse(args[3]);
            String description = Arrays.stream(Arrays.copyOfRange(args, 4, args.length))
                    .reduce("", (a, b) -> a.concat(" ").concat(b));
            Transaction transaction = manager.createTransaction(accountId, amount, effectiveDate, description);
            System.out.println("Created new transaction with id: " + transaction.getId());
        } else if("list".endsWith(args[0])) {
            List<Transaction> transactions = manager.list();
            transactions.stream().forEach(a ->
                    System.out.println("Transaction Id: " + a.getId() + ", " +
                        "Acct " + a.getAccount().getId() + ", " +
                        "Amount " + a.getAmount() + ", " +
                        "Effective Date " + a.getEffectiveDate()
                    ));
        }

        HibernateUtil.closeSessionFactory();
    }

    @SuppressWarnings("unchecked")
    private List<Transaction> list() {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List result = session.createQuery("from Transaction").list();
        session.getTransaction().commit();
        return result;
    }

    private Transaction createTransaction(Long accountId, Double amount, Date effectiveDate, String description) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();

        Account account = (Account) session.get(Account.class, accountId);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setEffectiveDate(effectiveDate);
        transaction.setDescription(description);
        transaction.setAccount(account);

        session.save(transaction);
        session.getTransaction().commit();

        return transaction;
    }
}
