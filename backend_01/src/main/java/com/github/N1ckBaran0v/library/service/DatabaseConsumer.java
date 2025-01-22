package com.github.N1ckBaran0v.library.service;

import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DatabaseConsumer {
    void accept(@NotNull Connection connection) throws SQLException;
}
