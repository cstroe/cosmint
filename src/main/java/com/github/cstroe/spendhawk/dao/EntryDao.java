package com.github.cstroe.spendhawk.dao;

import com.github.cstroe.spendhawk.api.Entry;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.LocalDate;

//@Entity
//@Table(name = "entry")
@Data
@NoArgsConstructor
public class EntryDao implements Entry {
    private static final String CREDIT = "credit";
    private static final String DEBIT = "debit";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private Double value;

    @Column
    private String currency;

    @Override
    public MonetaryAmount getAmount() {
        return Money.of(getValue(), getCurrency());
    }

    @Column
    private LocalDate transactionDate;

    @Column
    private LocalDate postedDate;

    @Column
    private String description;

    @Column
    private String type;

    @Override
    public boolean isCredit() {
        return CREDIT.equals(getType());
    }

    @Override
    public boolean isDebit() {
        return DEBIT.equals(getType());
    }
}
