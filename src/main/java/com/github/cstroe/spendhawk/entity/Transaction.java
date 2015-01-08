package com.github.cstroe.spendhawk.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * A monetary transaction against an account.  Implemented as a JavaBean.
 * </p>
 *
 * A transaction has:
 * <ul>
 *     <li>an amount, can be positive or negative, two decimal places of precision</li>
 *     <li>an effective date and time, at which the expense is applied to an account</li>
 *     <li>an account against which this transaction is executed</li>
 *     <li>a description</li>
 *     <li>some notes about this description</li>
 * </ul>
 */
public class Transaction {
    private Long id;
    private Account account;
    private Double amount;
    private Date effectiveDate;
    private String description;
    private String notes;

    public Transaction() {}

    public Long getId() {
        return id;
    }
    private void setId(Long id) {
        this.id = id;
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

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}