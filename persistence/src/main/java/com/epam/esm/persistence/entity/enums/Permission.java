package com.epam.esm.persistence.entity.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Permission {
    READ_CERTIFICATES("read_certificates"),
    READ_TAGS("read_tags"),

    READ_ORDERS("read_orders"),
    READ_ALL_ORDERS("read_all_orders"),
    READ_USERS("read_users"),

    WRITE_USERS("write_users"),
    WRITE_TAGS("write_tags"),
    WRITE_CERTIFICATES("write_certificates"),
    WRITE_ORDERS("write_orders");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(permission);
    }
}
