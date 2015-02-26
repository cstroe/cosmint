package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Transaction;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.BaseIT;
import com.github.cstroe.spendhawk.util.Exceptions;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryManagerBeanIT extends BaseIT {

    @Inject
    private CategoryManagerBean categoryManager;
    
    @Test
    public void testCreateCategory() {
        String categoryName = "New Category";
        Optional<Category> categoryOptional = categoryManager.createCategory(1l, categoryName);
        assertTrue("Category should be created.", categoryOptional.isPresent());

        Category category = categoryOptional.get();
        assertEquals("Category name should be correct.", categoryName, category.getName());
        assertEquals("User should be correct.", 1l, (long)category.getUser().getId());

        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Category retrievedCategory = Category.findById(currentUser, category.getId())
            .orElseThrow(Exceptions::categoryNotFound);
        assertEquals("Category name should be persisted correctly.", categoryName, retrievedCategory.getName());
        assertEquals("Category user should be persisted correctly.", currentUser.getId(), retrievedCategory.getUser().getId());
        commitTransaction();
    }

    @Test
    public void testCreateCategoryWithParent() {
        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Category parentCategory = Category.findById(currentUser, 1l)
            .orElseThrow(Exceptions::categoryNotFound);

        String categoryName = "New Category";
        Optional<Category> categoryOptional = categoryManager.createCategory(1l, categoryName, parentCategory);
        assertTrue("Category should be created.", categoryOptional.isPresent());

        Category category = categoryOptional.get();
        assertEquals("Category name should be correct.", categoryName, category.getName());
        assertEquals("User should be correct.", 1l, (long)category.getUser().getId());
        assertEquals("Parent category should be correct.",
            parentCategory.getId(), category.getParent().getId());
        commitTransaction();

        startTransaction();
        Category retrievedCategory = Category.findById(currentUser, category.getId())
                .orElseThrow(Exceptions::categoryNotFound);
        assertEquals("Category name should be persisted correctly.",
                categoryName, retrievedCategory.getName());
        assertEquals("Category user should be persisted correctly.",
                currentUser.getId(), retrievedCategory.getUser().getId());
        assertEquals("Parent category should be correct.",
                parentCategory.getId(), retrievedCategory.getParent().getId());
        commitTransaction();
    }

    @Test
    public void testCategoryGetChildren() {
        startTransaction();
        User currentUser = User.findById(1l).orElseThrow(Exceptions::userNotFound);
        Category parentCategory = Category.findById(currentUser, 1l)
                .orElseThrow(Exceptions::categoryNotFound);

        categoryManager.createCategory(1l, "Child Category 1", parentCategory)
            .orElseThrow(Exceptions::cannotCreateCategory);
        categoryManager.createCategory(1l, "Child Category 2", parentCategory)
                .orElseThrow(Exceptions::cannotCreateCategory);
        commitTransaction();

        assertEquals("The category object should be updated.",
            2, parentCategory.getChildren().size());

        startTransaction();
        Category retrievedParentCategory = Category.findById(parentCategory.getId())
            .orElseThrow(Exceptions::categoryNotFound);

        assertEquals("The parent category should have the correct children.",
            2, retrievedParentCategory.getChildren().size());
        commitTransaction();
    }

    @Test
    public void testCreateCategoryWithBlankName() {
        assertFalse("Should not create category with blank name.",
            categoryManager.createCategory(1l, "").isPresent());
        assertEquals("Category name cannot be blank.", categoryManager.getMessage());

        assertFalse("Should not create category with only spaces in name.",
            categoryManager.createCategory(1l, "    ").isPresent());

        assertFalse("Should not create category with only tabs in name.",
                categoryManager.createCategory(1l, "\t\t\t").isPresent());

        assertFalse("Should not create category with only whitespace in name.",
                categoryManager.createCategory(1l, "\t\t   \t\t   ").isPresent());

        assertFalse("Should not create category with null string.",
                categoryManager.createCategory(1l, null).isPresent());
    }

    @Test
    public void testCreateMaliciousCategoryName() {
        Category category = categoryManager.createCategory(1l, "</a><a href=\"test\">some name</a>")
            .orElseThrow(Exceptions::cannotCreateCategory);

        assertFalse(category.getName().contains("<"));
        assertFalse(category.getName().contains(">"));

        category = categoryManager.createCategory(1l, "' or 1=1")
                .orElseThrow(Exceptions::cannotCreateCategory);

        assertFalse(category.getName().contains("'"));
    }

    @Test
    public void testDeleteCategory() {
        boolean categoryDeleted = categoryManager.deleteCategory(1l, 1l);
        assertTrue("Category should be deleted.", categoryDeleted);

        startTransaction();
        assertFalse("Category deletion should be persisted.",
            Category.findById(1l).isPresent());

        Transaction t1 = Transaction.findById(1l).orElseThrow(RuntimeException::new);
        assertFalse("Should not have expenses for categories that have been deleted.",
            t1.getExpenses().stream().anyMatch(e -> e.getCategory().getId().equals(1l)));

        Transaction t2 = Transaction.findById(2l).orElseThrow(RuntimeException::new);
        assertFalse("Should not have expenses for categories that have been deleted.",
            t2.getExpenses().stream().anyMatch(e -> e.getCategory().getId().equals(1l)));

        Transaction t10 = Transaction.findById(10l).orElseThrow(RuntimeException::new);
        assertFalse("Should not have expenses for categories that have been deleted.",
            t10.getExpenses().stream().anyMatch(e -> e.getCategory().getId().equals(1l)));
        commitTransaction();
    }
}
