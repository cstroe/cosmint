package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.entity.Transaction;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Integer> {
    Optional<Transaction> findById(Integer id);
}
