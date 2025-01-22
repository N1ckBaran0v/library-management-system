package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

public interface UsersRepository {
    Optional<User> findByUsername(@NotNull Connection connection, @NotNull String username) throws SQLException;
    void deleteByUsername(@NotNull Connection connection, @NotNull String username) throws SQLException;
    void createUser(@NotNull Connection connection, @NotNull User user) throws SQLException;
}
