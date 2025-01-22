package com.github.N1ckBaran0v.library.context;

import com.github.N1ckBaran0v.library.service.*;
import org.jetbrains.annotations.NotNull;

public class ServiceContext {
    private final BookService bookService;
    private final UserService userService;

    public ServiceContext(@NotNull DatabaseContext databaseContext) {
        var databaseService = new DatabaseServiceImplementation(
                databaseContext.getDatabaseManager(),
                databaseContext.getBooksRepository(),
                databaseContext.getHistoryRepository(),
                databaseContext.getUsersRepository()
        );
        bookService = new BookServiceImplementation(databaseService);
        userService = new UserServiceImplementation(databaseService);
    }

    public BookService getBookService() {
        return bookService;
    }

    public UserService getUserService() {
        return userService;
    }
}
