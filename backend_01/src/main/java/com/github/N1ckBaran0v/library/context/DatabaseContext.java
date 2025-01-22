package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.database.DatabaseManager;
import com.github.N1ckBaran0v.library.database.SQLiteManager;
import com.github.N1ckBaran0v.library.repository.*;

public class DatabaseContext {
    private final BooksRepository booksRepository;
    private final HistoryRepository historyRepository;
    private final UsersRepository usersRepository;
    private final DatabaseManager databaseManager;

    public DatabaseContext() {
        this.booksRepository = new SQLiteBooksRepository();
        this.historyRepository = new SQLiteHistoryRepository();
        this.usersRepository = new SQLiteUsersRepository();
        this.databaseManager = new SQLiteManager("database", 4);
    }

    public BooksRepository getBooksRepository() {
        return booksRepository;
    }

    public HistoryRepository getHistoryRepository() {
        return historyRepository;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
