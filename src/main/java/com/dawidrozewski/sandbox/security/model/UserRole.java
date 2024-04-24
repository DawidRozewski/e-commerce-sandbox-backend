package com.dawidrozewski.sandbox.security.model;

import lombok.Getter;

@Getter
public enum UserRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_CUSTOMER("CUSTOMER");

    private final String role;

    UserRole(String role) {
        this.role = role;
    }
}
