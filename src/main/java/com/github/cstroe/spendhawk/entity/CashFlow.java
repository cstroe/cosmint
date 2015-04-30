package com.github.cstroe.spendhawk.entity;

/**
 * A CashFlow represents the change in value in a single account.  Value might
 * be put in to the account or might be taken out of the account.
 */
public class CashFlow {
    private Long id;
    private Transaction transaction;
    private Account account;
    private Double amount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
