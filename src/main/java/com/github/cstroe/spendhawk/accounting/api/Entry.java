package com.github.cstroe.spendhawk.accounting.api;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

/**
 * An entry in an account.
 */
public interface Entry {
    MonetaryAmount getAmount();
    LocalDate getTransactionDate();
    LocalDate getPostedDate();
    String getDescription();
    boolean isCredit();
    boolean isDebit();
}
