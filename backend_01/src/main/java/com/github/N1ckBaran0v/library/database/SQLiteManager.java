package com.github.N1ckBaran0v.library.database;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SQLiteManager implements DatabaseManager {
    private static final String SCHEMA_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "schema.sql";
    private static final String DATA_PATH = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "data.sql";

    private final List<Connection> pool;
    private final Semaphore semaphore;

    public SQLiteManager(@NotNull String databaseName, int poolSize) {
        try {
            Class.forName("org.sqlite.JDBC");
            var connName = String.format("jdbc:sqlite:%s.sqlite", databaseName);
            pool = new ArrayList<>(poolSize);
            for (var i = 0; i < poolSize; ++i) {
                var conn = DriverManager.getConnection(connName);
                conn.setAutoCommit(false);
                pool.add(conn);
            }
            for (var i = 0; i < poolSize; ++i) {
                var conn = pool.get(i);
                if (conn.isClosed()) {
                    System.out.println("bebra2");
                }
            }
            semaphore = new Semaphore(poolSize);
            returnConnection(getConnection());
            executeScript(SCHEMA_PATH);
            executeScript(DATA_PATH);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection(@NotNull String connName) throws SQLException {
        return DriverManager.getConnection(connName);
    }

    @Override
    public Connection getConnection() {
        try {
            semaphore.acquire();
            synchronized (pool) {
                return pool.removeLast();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void returnConnection(@NotNull Connection connection) {
        synchronized (pool) {
            pool.add(connection);
        }
        semaphore.release();
    }

    @Override
    public void close() throws Exception {
        for (var conn : pool) {
            conn.close();
        }
    }

    private void executeScript(@NotNull String filename) throws Exception {
        var script = new String(Files.readAllBytes(Path.of(filename)));
        var connection = getConnection();
        try {
            for (var command : script.split(";")) {
                if (command.isBlank()) {
                    continue;
                }
                connection.createStatement().execute(command + ";");
            }
            connection.commit();
        } catch (Exception e) {
            connection.rollback();
            throw e;
        } finally {
            returnConnection(connection);
        }
    }
}
