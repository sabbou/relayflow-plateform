package com.relayflow.backend.user;

import jakarta.persistence.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table (name = "users")
public class User {
    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role = Role.USER;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public User() {
    }

    public User(Role role, String passwordHash, String email, UUID id) {
        this.role = role;
        this.passwordHash = passwordHash;
        this.email = email;
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }
}
