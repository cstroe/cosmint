package com.github.cstroe.spendhawk.entity;

import com.github.cstroe.spendhawk.util.BaseIT;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
public class ExpenseEntityIT extends BaseIT {

    @Test
    public void testFindByCategory() {
        startTransaction();
        User currentUser = User.findById(1l)
            .orElseThrow(() -> new RuntimeException("Cannot find user 1 (it should exist in seed.xml)."));

        Category category = Category.findById(currentUser, 1l)
            .orElseThrow(() -> new RuntimeException("Cannot find category 1 (it should exist in seed.xml)."));

        List<Expense> expenseList = Expense.findByCategory(category);
        assertEquals("There should be 3 expenses with the given category.",
            3, expenseList.size());
        commitTransaction();
    }
}
