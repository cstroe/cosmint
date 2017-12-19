package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(Long id);
}
