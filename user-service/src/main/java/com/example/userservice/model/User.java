package com.example.userservice.model;

import com.example.userservice.enums.Active;
import com.example.userservice.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.HashSet;
import java.util.List;

@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, updatable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Role primaryRole = Role.USER; // Role chính

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role")
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private Active active = Active.ACTIVE;

    @Embedded
    private UserDetails userDetails;
    
    // Helper methods
    public void addRole(Role role) {
        this.roles.add(role);
        if (this.primaryRole == Role.USER) {
            this.primaryRole = role;
        }
    }
    
    public void removeRole(Role role) {
        this.roles.remove(role);
        if (this.primaryRole == role && !this.roles.isEmpty()) {
            this.primaryRole = this.roles.iterator().next();
        } else if (this.roles.isEmpty()) {
            this.primaryRole = Role.USER;
        }
    }
    
    public boolean hasRole(Role role) {
        return this.roles.contains(role);
    }

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> vetAppointments;

    @OneToMany(mappedBy = "vet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HealthRecord> vetHealthRecords;
}
