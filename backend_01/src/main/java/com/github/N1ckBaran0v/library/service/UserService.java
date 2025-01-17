package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.jetbrains.annotations.NotNull;

public interface UserService {
    void register(@NotNull RegisterForm form, @NotNull String sessionId);
    void login(@NotNull LoginForm form, @NotNull String sessionId);
    void logout(@NotNull String sessionId);
    void deleteUser(@NotNull String sessionId, @NotNull String username);
    String getUsername(@NotNull String sessionId);
    User getUser(@NotNull String sessionId);
    User getUser(@NotNull String sessionId, @NotNull String username);
}
