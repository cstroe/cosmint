package com.github.cstroe.spendhawk.repository;

import com.github.cstroe.spendhawk.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findById(Integer id);
}
