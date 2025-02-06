package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.LoginForm;
import com.github.N1ckBaran0v.library.form.RegisterForm;
import org.jetbrains.annotations.NotNull;

public interface UserService {
    void register(@NotNull SessionInfo sessionInfo, @NotNull RegisterForm form);
    void login(@NotNull SessionInfo sessionInfo, @NotNull LoginForm form);
    void logout(@NotNull SessionInfo sessionInfo);
    void deleteUser(@NotNull SessionInfo sessionInfo, @NotNull String username);
    User getUser(@NotNull SessionInfo sessionInfo, @NotNull String username);
}
