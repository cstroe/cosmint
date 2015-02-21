package com.github.cstroe.spendhawk.util;

import java.util.HashSet;
import java.util.Set;

public class Exceptions {
    public static RuntimeException transactionIdRequired() {
        return new RuntimeException("Transaction id is required.");
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
}
