package com.IngHubTurkey.digital_wallet.type;

public enum UserRole {
    CUSTOMER,
    EMPLOYEE;

    public static UserRole from(String role) {
        try {
            return UserRole.valueOf(role.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid user role: " + role);
        }
    }
}
