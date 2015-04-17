package com.github.cstroe.spendhawk.entity;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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

    @Test
    public void testCategoryCompareToSort() {
        char c = 'z';

        List<Category> categoryList = new ArrayList<>();
        for (long i = 0; i <= 'z' - 'a'; i++) {
            Category currentCategory = new Category();
            currentCategory.setId(i);
            currentCategory.setName("Category " + Character.toString((char) (c - i)));
            categoryList.add(currentCategory);
        }

        Collections.shuffle(categoryList);
        Collections.sort(categoryList);

        for (int i = 0; i < categoryList.size() - 1; i++) {
            Category currentCategory = categoryList.get(i);
            Category nextCategory = categoryList.get(i + 1);
            assertThat("A current category should be before the next category",
                currentCategory.compareTo(nextCategory), is(-1));
            assertThat("A next category should be after the current category",
                nextCategory.compareTo(currentCategory), is(1));
        }
    }

    @Test
    public void testCategoryCompareToEquality() {
        Category c1 = new Category();
        c1.setId(2l);
        c1.setName("A");

        Category c2 = new Category();
        c2.setId(1l);
        c2.setName("A");

        assertThat(c1.compareTo(c2), is(0));
    }
}