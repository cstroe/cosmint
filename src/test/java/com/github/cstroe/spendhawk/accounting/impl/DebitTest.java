package com.github.cstroe.spendhawk.accounting.impl;

import com.github.cstroe.spendhawk.impl.Debit;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DebitTest {
    @Test
    public void defaultMethods() {
        Debit blankDebit = new Debit();
        assertTrue(blankDebit.isDebit());
        assertFalse(blankDebit.isCredit());
    }
}