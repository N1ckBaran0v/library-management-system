package com.github.N1ckBaran0v.library.form;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

public class RegisterForm implements Checkable {
    private String username = "";
    private String password = "";
    private String confirmPassword = "";
    private String name = "";
    private String phone = "";
    private String role = User.USER_ROLE;

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_-]{6,16}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^8\\(9[0-9]{2}\\) [0-9]{3}-[0-9]{2}-[0-9]{2}$");

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@NotNull String confirmPassword) {
        this.confirmPassword = confirmPassword;
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

    @Override
    public boolean hasErrors() {
        var answer = false;
        if (!USERNAME_PATTERN.matcher(username).matches() || !PHONE_PATTERN.matcher(phone).matches()) {
            answer = true;
        } else if (password.isBlank() || name.isBlank() || !(role.equals("user") || role.equals("worker"))) {
            answer = true;
        } else {
            answer = !password.equals(confirmPassword);
        }
        return answer;
    }
}
