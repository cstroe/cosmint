package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.TransactionDao;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TransactionRepository extends CrudRepository<TransactionDao, Long> {
    Optional<TransactionDao> findById(Long id);

    @Query("SELECT t FROM TransactionDao t INNER JOIN t.entries e WITH e.id = :entryId")
    Optional<TransactionDao> findByEntry(@Param("entryId") Long entryId);
}
