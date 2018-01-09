package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.dao.UserDao;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDao, Long> {
    UserDao findById(Long id);
}
