package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.AccountDao;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<AccountDao, Integer> {
    Optional<AccountDao> findByIdAndUserId(Long accountId, Long userId);
    Optional<AccountDao> findByName(String name);

    <T> Collection<T> findByUserId(Long userId, Class<T> type);
}
