package com.github.cstroe.spendhawk.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * <p>
 * A monetary transaction against accounts, implemented as a collection of
 * cash flows.
 * </p>
 *
 * A transaction has:
 * <ul>
 *     <li>some notes about this transaction</li>
 *     <li>a collection of cash flows</li>
 * </ul>
 */
@Entity
@Table(name = "transactions")
@Data
public class Transaction {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column
    private Double amount;

    @Column
    private String description;

    @Column
    private String notes;

    @ManyToOne
    @JoinColumn(name = "source_account_id")
    private Account source;

    @ManyToOne
    @JoinColumn(name = "target_account_id")
    private Account target;
}
