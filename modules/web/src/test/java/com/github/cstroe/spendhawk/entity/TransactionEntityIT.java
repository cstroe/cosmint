package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class TransactionEntityIT extends BaseIT {

    @Test
    public void testFindById() {
        startTransaction();
        Transaction.findById(1l).orElseThrow(RuntimeException::new);
        Transaction.findById(2l).orElseThrow(RuntimeException::new);
        Transaction.findById(10l).orElseThrow(RuntimeException::new);
        Transaction.findById(12l).orElseThrow(RuntimeException::new);
        commitTransaction();
    }
}
