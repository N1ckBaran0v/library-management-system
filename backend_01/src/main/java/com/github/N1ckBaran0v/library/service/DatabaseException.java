package com.github.N1ckBaran0v.library.service;

import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DatabaseException extends RuntimeException {
    public DatabaseException(@NotNull SQLException e) {
        super(e);
    }
}
