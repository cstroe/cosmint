package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.TransactionDao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TransactionRepository {//extends CrudRepository<TransactionDao, Integer> {
    Optional<TransactionDao> findById(Integer id);
}
