package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.entity.Category;
import com.github.cstroe.spendhawk.entity.Expense;
import com.github.cstroe.spendhawk.entity.User;
import com.github.cstroe.spendhawk.util.Exceptions;

import javax.ejb.Stateful;
import javax.inject.Inject;
import java.util.Optional;

@Stateful
public class CategoryManagerBean extends DatabaseBean {

    private String message;

    @Inject
    private JanitorBean janitor;

    public String getMessage() {
        return message;
    }

    public Optional<Category> createCategory(Long userId, String categoryName) {
        return createCategory(userId, categoryName, null);
    }

    public Optional<Category> createCategory(Long userId, String categoryName, Category parentCategory) {
        if(janitor.isBlank(categoryName)) {
            message = "Category name cannot be blank.";
            return Optional.empty();
        }

        categoryName = janitor.sanitize(categoryName);

        try {
            startTransaction();
            User user = User.findById(userId).orElseThrow(Exceptions::userNotFound);
            Category newCategory = new Category();
            newCategory.setName(categoryName);
            newCategory.setUser(user);
            newCategory.setParent(parentCategory);
            newCategory.save();
            commitTransaction();
            if(parentCategory != null) {
                parentCategory.getChildren().add(newCategory);
            }
            return Optional.of(newCategory);
        } catch(Exception ex) {
            rollbackTransaction();
            message = ex.getMessage();
            return Optional.empty();
        }
    }

    public boolean deleteCategory(Long userId, Long categoryId) {
        try {
            startTransaction();
            User user = User.findById(userId).orElseThrow(Exceptions::userNotFound);
            Category categoryToDelete = Category.findById(user, categoryId)
                .orElseThrow(Exceptions::categoryNotFound);
            Expense.findByCategory(categoryToDelete).stream().forEach(Expense::delete);
            categoryToDelete.delete();
            commitTransaction();
            return true;
        } catch(Exception ex) {
            rollbackTransaction();
            message = ex.getMessage();
            return false;
        }
    }

    public boolean setParent(Long userId, Long categoryId, Long parentCategoryId) {
        try {
            startTransaction();
            User user = User.findById(userId).orElseThrow(Exceptions::userNotFound);
            Category category = Category.findById(user, categoryId)
                .orElseThrow(Exceptions::categoryNotFound);
            Category parentCategory = Category.findById(user, parentCategoryId)
                .orElseThrow(Exceptions::parentCategoryNotFound);
            category.setParent(parentCategory);
            category.save();
            commitTransaction();
            return true;
        } catch(Exception ex) {
            rollbackTransaction();
            message = ex.getMessage();
            return false;
        }
    }
}
