package com.epam.esm.persistence.entity.enums;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    private String role;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_permissions",
            joinColumns = {
                    @JoinColumn(name = "role")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "permission")
            }
    )
    private Set<Permission> rolePermissions;

    public Role() {
    }

    public Role(String role, Set<Permission> rolePermissions) {
        this.role = role;
        this.rolePermissions = rolePermissions;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setRolePermissions(Set<Permission> rolePermissions) {
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
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }
}

