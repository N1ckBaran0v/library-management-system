package com.github.N1ckBaran0v.library.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Set;

@Entity
public class User implements Serializable {
    @Id
    private String username;
    private String password;
    private String name;
    private String phone;
    private String role;

    public static final String UNAUTHORIZED = "unauthorized";
    public static final String USER_ROLE = "user";
    public static final String WORKER_ROLE = "worker";
    public static final String ADMIN_ROLE = "admin";

    public static final Set<String> ROLES = Set.of(USER_ROLE, WORKER_ROLE, ADMIN_ROLE, UNAUTHORIZED);

    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@NotNull String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }
}
