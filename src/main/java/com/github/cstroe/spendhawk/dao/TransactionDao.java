package com.github.cstroe.spendhawk.dao;

import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

/**
 * A monetary transaction against accounts.
 */
@Entity
@NamedQuery(name = "TransactionDao.getNames",
        query = "select acct.name from AccountDao acct where acct.user.id = ?1")
@Table(name = "transaction")
@Data
public class TransactionDao {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(targetEntity = EntryDao.class)
    @JoinTable(name = "transaction_to_entry",
        joinColumns = { @JoinColumn(name = "transaction_id", referencedColumnName = "id") },
        inverseJoinColumns = { @JoinColumn(name = "entry_id", referencedColumnName = "id") }
    )
    private Collection<EntryDao> entries;
}
