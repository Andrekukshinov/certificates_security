package com.epam.esm.persistence.entity.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(
            Permission.READ_TAGS,
            Permission.READ_USERS,
            Permission.READ_ORDERS,
            Permission.READ_CERTIFICATES,
            Permission.WRITE_ORDERS
    )),
    ADMIN(Set.of(
            Permission.READ_TAGS,
            Permission.READ_USERS,
            Permission.READ_ORDERS,
            Permission.READ_CERTIFICATES,
            Permission.WRITE_ORDERS,
            Permission.READ_ALL_ORDERS,
            Permission.WRITE_CERTIFICATES,
            Permission.WRITE_USERS,
            Permission.WRITE_TAGS

    ));

    private final Set<Permission> rolePermissions;

    Role(Set<Permission> rolePermissions) {
        this.rolePermissions = rolePermissions;
    }

    public Set<Permission> getRolePermissions() {
        return rolePermissions;
    }

    public Set<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = rolePermissions
                .stream()
                .map(Permission::getAuthority)
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + name()));
        return authorities;
    }
}

