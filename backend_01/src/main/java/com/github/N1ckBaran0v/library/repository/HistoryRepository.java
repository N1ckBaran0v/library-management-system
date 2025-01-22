package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.History;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface HistoryRepository {
    List<History> getUserHistory(@NotNull Connection connection, @NotNull String username) throws SQLException;
    void getBooks(@NotNull Connection connection, @NotNull List<History> history) throws SQLException;
    void returnBooks(@NotNull Connection connection, @NotNull List<History> history) throws SQLException;
}
