package com.epam.esm.persistence.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    private String permission;

    public Permission() {
    }

    public Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public GrantedAuthority getAuthority() {
        return new SimpleGrantedAuthority(permission);
    }
}
