package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.entity.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Integer> {
    Optional<Account> findByIdAndUserId(Integer accountId, Integer userId);
    Optional<Account> findByName(String name);
    List<Account> findByUserId(Integer userId);
}