package com.github.cstroe.spendhawk.helper;

import com.github.cstroe.spendhawk.entity.Transaction;

import java.util.Collection;

/**
 * Wraps a collection of transactions and provides aggregate methods on them.
 */
public class TListTotaler {

    private final Collection<Transaction> transactionList;

    public TListTotaler(Collection<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public double getTotal() {
        return transactionList.stream().mapToDouble(Transaction::getAmount).sum();
    }
}
