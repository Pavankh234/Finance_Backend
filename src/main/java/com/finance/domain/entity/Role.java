package com.finance.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    public enum RoleName {
        VIEWER, ANALYST, ADMIN
    }

    public enum Permission {
        USER_READ, USER_CREATE, USER_UPDATE, USER_DELETE,
        RECORD_READ, RECORD_CREATE, RECORD_UPDATE, RECORD_DELETE,
        DASHBOARD_VIEW, DASHBOARD_ANALYTICS, ROLE_MANAGE
    }

    public boolean hasPermission(Permission permission) {
        return permissions.contains(permission);
    }
}
