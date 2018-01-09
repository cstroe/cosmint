package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.AccountDao;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<AccountDao, Integer> {
    Optional<AccountDao> findByIdAndUserId(Long accountId, Long userId);
    Optional<AccountDao> findByName(String name);
    List<AccountDao> findByUserId(Long userId);
}
