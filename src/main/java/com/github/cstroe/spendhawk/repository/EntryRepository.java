package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.EntryDao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends CrudRepository<EntryDao, Long> {
    Optional<EntryDao> findByIdAndAccountId(Long id, Long accountId);

    List<EntryDao> findByAccountId(Long accountId);

    List<EntryDao> findByAccountIdAndDescriptionContainingIgnoreCase(Long accountId, String term);
}
