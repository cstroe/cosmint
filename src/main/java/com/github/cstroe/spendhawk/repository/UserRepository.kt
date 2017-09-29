package com.github.cstroe.spendhawk.repository

import com.github.cstroe.spendhawk.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Long> {
    fun findById(id: Long): User?
}