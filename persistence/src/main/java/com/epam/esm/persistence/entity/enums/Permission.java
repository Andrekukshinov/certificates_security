package com.epam.esm.persistence.entity.enums;

public enum Permission {
    //fixme
    USERS_WRITE("users_write"), USERS_READ("users_read");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
