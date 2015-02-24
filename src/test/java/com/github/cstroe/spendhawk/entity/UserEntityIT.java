package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertFalse;

@RunWith(Arquillian.class)
public class UserEntityIT extends BaseIT {

    @Test
    public void testFindById() {
        startTransaction();
        User.findById(1l).orElseThrow(Exceptions::userNotFound);
        commitTransaction();
    }

    @Test
    public void testFindByIdWithBadId() {
        startTransaction();
        assertFalse("Should not find non-existent user.",
            User.findById(2l).isPresent());

        assertFalse("Should not find null id",
            User.findById(null).isPresent());
        commitTransaction();
    }
}
