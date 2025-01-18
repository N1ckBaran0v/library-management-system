package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;

public interface DatabaseService {
    User findUserByUsername(@NotNull String username);
    void deleteUserByUsername(@NotNull String username);
    void createUser(@NotNull User user);
}
