package com.github.cstroe.spendhawk.dao;

import com.github.cstroe.spendhawk.api.Entry;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;

@Entity
@Table(name = "entry")
@Data
@NoArgsConstructor
public class EntryDao implements Entry {
    private static final DecimalFormat FORMAT = new DecimalFormat("#,##0.00");
    private static final String CREDIT = "credit";
    private static final String DEBIT = "debit";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, scale = 15, precision = 2)
    private BigDecimal value;

    @Column(name = "currency_code", nullable = false)
    private String currency;

    @Override
    public MonetaryAmount getAmount() {
        return Money.of(getValue(), getCurrency());
    }

    public String getDisplayAmount() {
        return FORMAT.format(getValue());
    }

    public void setAmount(MonetaryAmount amount) {
        this.setValue(new BigDecimal(amount.getNumber().doubleValue()));
        this.setCurrency(amount.getCurrency().getCurrencyCode());
    }

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate;

    @Column
    private String description;

    @Column(name = "entry_type", nullable = false)
    private String type;

    @Override
    public boolean isCredit() {
        return CREDIT.equals(getType());
    }

    @Override
    public boolean isDebit() {
        return DEBIT.equals(getType());
    }

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name="account_id", nullable = false)
    private AccountDao account;

    public static EntryDao create(Entry entry) {
        EntryDao dao = new EntryDao();
        dao.setAmount(entry.getAmount());
        dao.setTransactionDate(entry.getTransactionDate());
        dao.setPostedDate(entry.getPostedDate());
        dao.setDescription(entry.getDescription());

        if(entry.isCredit()) {
            dao.setType(CREDIT);
        }

        if(entry.isDebit()) {
            dao.setType(DEBIT);
        }

        return dao;
    }
}
