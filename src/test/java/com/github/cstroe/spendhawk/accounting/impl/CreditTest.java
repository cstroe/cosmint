package com.github.cstroe.spendhawk.accounting.impl;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CreditTest {
    @Test
    public void defaultMethods() {
        Credit blankCredit = new Credit();
        assertTrue(blankCredit.isCredit());
        assertFalse(blankCredit.isDebit());
    }
}