package com.github.cstroe.spendhawk.util;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Keep track of our exceptions.
 */
public final class Ex {

    public static Supplier<RuntimeException> ception(final String message) {
        //noinspection ThrowableInstanceNeverThrown
        return () -> new RuntimeException(message);
    }

    public static RuntimeException transactionIdRequired() {
        return new RuntimeException("Transaction id is required.");
    }

    public static RuntimeException accountNameRequired() {
        return new RuntimeException("Account name is required.");
    }

    public static RuntimeException accountIdRequired() {
        return new RuntimeException("Account id is required.");
    }

    public static RuntimeException transactionNotFound() {
        return new RuntimeException("Transaction not found.");
    }

    public static RuntimeException accountNotFound() {
        return new RuntimeException("Account not found.");
    }

    public static RuntimeException userNotFound() {
        return new RuntimeException("User not found.");
    }

    public static RuntimeException userIdRequired() {
        return new RuntimeException("User id required.");
    }

    public static String getDescriptiveMessage(Throwable ex) {
        StringBuilder sb = new StringBuilder();
        sb.append(ex.getMessage());
        Set<Throwable> avoidCycles = new HashSet<>();
        while(!avoidCycles.contains(ex) && ex.getCause() != null) {
            sb.append("; ");
            sb.append(ex.getCause());
            avoidCycles.add(ex);
            ex = ex.getCause();
        }
        return sb.toString();
    }

    public static RuntimeException categoryNotFound() {
        return new RuntimeException("Category not found.");
    }

    public static RuntimeException categoryIdNotSpecified() {
        return new RuntimeException("Category id not specified.");
    }

    public static RuntimeException parentCategoryNotFound() {
        return new RuntimeException("Parent category not found.");
    }

    public static RuntimeException cannotCreateCategory() {
        return new RuntimeException("Cannot create category.");
    }

    public static RuntimeException reportNotFound() {
        return new RuntimeException("Report not found.");
    }

    public static RuntimeException categoryParentNotSpecified() {
        return new RuntimeException("Parent category ID is not specified.");
    }

    public static RuntimeException actionNotSpecified() {
        return ception("Action not specified.").get();
    }

    public static RuntimeException cashFlowNotFound() {
        return new RuntimeException("CashFlow not found.");
    }
}
