package com.github.cstroe.spendhawk.entity;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class CashFlowTest {

    @Test
    public void equals_with_null_id() {
        CashFlow cf1 = new CashFlow();
        CashFlow cf2 = new CashFlow();

        assertFalse(cf1.equals(cf2));
        assertFalse(cf2.equals(cf1));
    }

    @Test
    public void equals_with_one_real_id_and_one_null() {
        CashFlow cf1 = new CashFlow();
        cf1.setId(1l);
        CashFlow cf2 = new CashFlow();

        assertFalse(cf1.equals(cf2));
        assertFalse(cf2.equals(cf1));
    }

    @Test
    public void equals() {
        CashFlow cf1 = new CashFlow();
        cf1.setId(1l);
        CashFlow cf2 = new CashFlow();
        cf2.setId(2l);

        assertFalse(cf1.equals(cf2));
        assertFalse(cf2.equals(cf1));

        cf2.setId(1l);

        assertTrue(cf1.equals(cf2));
        assertTrue(cf2.equals(cf1));
    }

    @Test
    public void hashcode() {
        CashFlow cf1 = new CashFlow();
        cf1.setId(1l);
        CashFlow cf2 = new CashFlow();
        cf2.setId(1l);

        assertTrue(cf1.hashCode() == cf2.hashCode());
    }
}
