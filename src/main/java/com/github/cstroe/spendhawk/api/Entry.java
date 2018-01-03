package com.github.cstroe.spendhawk.api;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

/**
 * An entry in an account.
 */
public interface Entry {
    Money getAmount();
    LocalDate getTransactionDate();
    LocalDate getPostedDate();
    String getDescription();
    boolean isCredit();
    boolean isDebit();
}
