package com.github.N1ckBaran0v.library.form;

import com.github.N1ckBaran0v.library.data.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;


public class RegisterForm {
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_-]{6,16}$")
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @NotNull
    @NotBlank
    private String confirmPassword;

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @Pattern(regexp = "^8\\(9[0-9]{2}\\) [0-9]{3}-[0-9]{2}-[0-9]{2}$")
    private String phone;

    @NotNull
    @NotBlank
    private String role = User.USER_ROLE;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
