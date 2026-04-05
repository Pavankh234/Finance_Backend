package com.finance.domain.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 20)
    private RoleName name;

    @Column(length = 100)
    private String description;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Set<Permission> permissions = new HashSet<>();

    public enum RoleName {
        VIEWER, ANALYST, ADMIN
    }

    public enum Permission {
        USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
        RECORD_READ, RECORD_CREATE, RECORD_UPDATE, RECORD_DELETE,
        DASHBOARD_VIEW, DASHBOARD_ANALYTICS, ROLE_MANAGE
    }

    public Role() {}

    public Role(Long id, RoleName name, String description, Set<Permission> permissions) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.permissions = permissions != null ? permissions : new HashSet<>();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public RoleName getName() { return name; }
    public void setName(RoleName name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Set<Permission> getPermissions() { return permissions; }
    public void setPermissions(Set<Permission> permissions) { this.permissions = permissions; }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }

    public static RoleBuilder builder() { return new RoleBuilder(); }

    public static class RoleBuilder {
        private Long id;
        private RoleName name;
        private String description;
        private Set<Permission> permissions = new HashSet<>();

        public RoleBuilder id(Long id) { this.id = id; return this; }
        public RoleBuilder name(RoleName name) { this.name = name; return this; }
        public RoleBuilder description(String description) { this.description = description; return this; }
        public RoleBuilder permissions(Set<Permission> permissions) { this.permissions = permissions; return this; }
        public Role build() { return new Role(id, name, description, permissions); }
    }
}
