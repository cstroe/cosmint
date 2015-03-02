package com.github.cstroe.spendhawk.entity;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class CategoryTest {
    @Test
    public void testCategoryEquals() {
        Category c1 = new Category();
        c1.setId(1l);

        Category c2 = new Category();
        c2.setId(1l);

        assertTrue(c1.equals(c2));
    }
}
