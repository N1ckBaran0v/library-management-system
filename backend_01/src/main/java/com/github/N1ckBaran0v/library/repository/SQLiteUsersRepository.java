package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.User;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class SQLiteUsersRepository implements UsersRepository {
    @Override
    public Optional<User> findByUsername(@NotNull Connection connection, @NotNull String username) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM users WHERE username = (?);")) {
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            var list = new ArrayList<User>();
            if (resultSet.next()) {
                var user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setName(resultSet.getString("name"));
                user.setPhone(resultSet.getString("phone"));
                user.setRole(resultSet.getString("role"));
                list.add(user);
            }
            return Optional.ofNullable(list.isEmpty() ? null : list.getFirst());
        }
    }

    @Override
    public void deleteByUsername(@NotNull Connection connection, @NotNull String username) throws SQLException {
        try (var statement = connection.prepareStatement("DELETE FROM users WHERE username = (?);")) {
            statement.setString(1, username);
            statement.executeQuery();
        }
    }

    @Override
    public void createUser(@NotNull Connection connection, @NotNull User user) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO users (username, password, name, phone, role) VALUES (?, ?, ?, ?, ?);")) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getPhone());
            statement.setString(5, user.getRole());
            statement.executeUpdate();
        }
    }
}
