package com.github.N1ckBaran0v.library.form;

import org.jetbrains.annotations.NotNull;

public class LoginForm implements Checkable {
    private String username = "";
    private String password = "";

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

    @Override
    public boolean hasErrors() {
        return username.isBlank() || password.isBlank();
    }
}
