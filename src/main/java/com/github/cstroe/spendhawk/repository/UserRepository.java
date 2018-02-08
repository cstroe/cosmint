package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.UserDao;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserDao, Long> {
    Optional<UserDao> findById(Long id);
}
