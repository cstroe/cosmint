package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.Ex;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class UserEntityIT {

    @Test
    public void testFindById() {
        User.findById(1L).orElseThrow(Ex::userNotFound);
    }

    @Test
    public void testFindByIdWithBadId() {
        assertFalse("Should not find non-existent user.",
            User.findById(2L).isPresent());

        assertFalse("Should not find null id",
            User.findById(null).isPresent());
    }
}
