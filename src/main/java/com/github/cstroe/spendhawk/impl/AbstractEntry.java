package com.github.cstroe.spendhawk.impl;

import com.github.cstroe.spendhawk.api.Entry;
import lombok.Data;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import java.time.LocalDate;

@Data
public abstract class AbstractEntry implements Entry {
    private Money amount;
    private LocalDate transactionDate;
    private LocalDate postedDate;
    private String description;
}
