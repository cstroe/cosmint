package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.EntryDao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EntryRepository extends CrudRepository<EntryDao, Long> {
    List<EntryDao> findByAccountId(Long accountId);

    List<EntryDao> findByAccountIdAndDescriptionContainingIgnoreCase(Long accountId, String term);
}
