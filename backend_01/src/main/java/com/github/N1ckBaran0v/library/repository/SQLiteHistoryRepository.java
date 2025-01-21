package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.History;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SQLiteHistoryRepository implements HistoryRepository {
    @Override
    public List<History> getUserHistory(@NotNull Connection connection, @NotNull String username) throws SQLException {
        try (var statement = connection.prepareStatement("SELECT * FROM history WHERE username = (?);")) {
            statement.setString(1, username);
            var resultSet = statement.executeQuery();
            var historyList = new ArrayList<History>();
            while (resultSet.next()) {
                var history = new History();
                history.setId(resultSet.getLong("id"));
                history.setUsername(resultSet.getString("username"));
                history.setBookId(resultSet.getLong("book_id"));
                history.setOperationType(resultSet.getString("operation_type"));
                history.setOperationTime(resultSet.getTimestamp("operation_time"));
                historyList.add(history);
            }
            return historyList;
        }
    }

    @Override
    public void getBooks(@NotNull Connection connection, @NotNull List<History> history) throws SQLException {
        saveHistory(connection, history);
    }

    @Override
    public void returnBooks(@NotNull Connection connection, @NotNull List<History> history) throws SQLException {
        saveHistory(connection, history);
    }

    private void saveHistory(@NotNull Connection connection, @NotNull List<History> history) throws SQLException {
        try (var statement = connection.prepareStatement("INSERT INTO history (username, book_id, operation_type, operation_time) VALUES (?, ?, ?, ?);")) {
            var timestamp = new Timestamp(System.currentTimeMillis());
            for (var item : history) {
                statement.setString(1, item.getUsername());
                statement.setLong(2, item.getBookId());
                statement.setString(3, item.getOperationType());
                statement.setTimestamp(4, timestamp);
                statement.executeUpdate();
            }
        }
    }
}
