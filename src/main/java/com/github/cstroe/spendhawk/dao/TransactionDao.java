package com.github.cstroe.spendhawk.dao;

import com.github.cstroe.spendhawk.api.Entry;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * A monetary transaction against accounts.
 */
//@Entity
//@Table(name = "transactions")
@Data
public class TransactionDao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String description;

    @OneToMany(targetEntity = EntryDao.class)
    private Collection<Entry> transfers;
}
