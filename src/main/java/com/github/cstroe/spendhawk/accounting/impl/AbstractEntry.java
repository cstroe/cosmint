package com.github.cstroe.spendhawk.accounting.impl;

import com.github.cstroe.spendhawk.accounting.api.Entry;
import lombok.Data;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

@Data
public abstract class AbstractEntry implements Entry {
    private MonetaryAmount amount;
    private LocalDate transactionDate;
    private LocalDate postedDate;
    private String description;
}
