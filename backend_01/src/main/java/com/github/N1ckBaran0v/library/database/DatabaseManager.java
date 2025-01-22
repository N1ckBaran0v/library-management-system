package com.github.N1ckBaran0v.library.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseManager extends AutoCloseable {
    Connection getConnection() throws SQLException;
    void returnConnection(@NotNull Connection connection);
}
