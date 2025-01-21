package com.github.N1ckBaran0v.library.database;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SQLiteManager implements DatabaseManager {
    private final List<Connection> pool;
    private final Semaphore semaphore;

    public SQLiteManager(@NotNull String databaseName, int poolSize) {
        try {
            Class.forName("org.sqlite.JDBC");
            var connName = String.format("jdbc:sqlite:%s.sqlite", databaseName);
            pool = new ArrayList<Connection>(poolSize);
            for(var i = 0; i < poolSize; ++i) {
                var conn = DriverManager.getConnection(connName);
                conn.setAutoCommit(false);
                pool.add(conn);
            }
            semaphore = new Semaphore(poolSize);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized Connection getConnection() {
        try {
            semaphore.acquire();
            return pool.removeLast();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void returnConnection(@NotNull Connection connection) {
        pool.add(connection);
        semaphore.release();
    }

    @Override
    public void close() throws Exception {
        for (var conn : pool) {
            conn.close();
        }
    }
}
