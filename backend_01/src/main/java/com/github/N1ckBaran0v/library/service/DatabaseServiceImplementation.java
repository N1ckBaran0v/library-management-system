package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.database.DatabaseManager;
import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.repository.BooksRepository;
import com.github.N1ckBaran0v.library.repository.HistoryRepository;
import com.github.N1ckBaran0v.library.repository.UsersRepository;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.List;
import java.util.TreeMap;

public class DatabaseServiceImplementation implements DatabaseService {
    private final DatabaseManager databaseManager;
    private final BooksRepository booksRepository;
    private final HistoryRepository historyRepository;
    private final UsersRepository usersRepository;

    public DatabaseServiceImplementation(@NotNull DatabaseManager databaseManager,
                                         @NotNull BooksRepository booksRepository,
                                         @NotNull HistoryRepository historyRepository,
                                         @NotNull UsersRepository usersRepository) {
        this.databaseManager = databaseManager;
        this.booksRepository = booksRepository;
        this.historyRepository = historyRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public User findUserByUsername(@NotNull String username) {
        return transactionalOperation(conn -> {
            return usersRepository.findByUsername(conn, username).orElseThrow(UserNotFoundException::new);
        });
    }

    @Override
    public void deleteUserByUsername(@NotNull String username) {
        transactionalOperation(conn -> {
            usersRepository.findByUsername(conn, username).orElseThrow(UserNotFoundException::new);
            usersRepository.deleteByUsername(conn, username);
        });
    }

    @Override
    public void createUser(@NotNull User user) {
        transactionalOperation(conn -> {
            usersRepository.findByUsername(conn, user.getUsername()).ifPresent(u -> {
                throw new UserAlreadyExistsException();
            });
            usersRepository.createUser(conn, user);
        });
    }

    @Override
    public List<Book> findBooksByParams(@NotNull SearchForm searchForm) {
        return transactionalOperation(conn -> {
            return booksRepository.findAllBooks(conn).stream().filter(book -> {
                var result = true;
                if (searchForm.getAuthor() != null && !book.getAuthor().equals(searchForm.getAuthor())) {
                    result = false;
                } else if (searchForm.getTitle() != null && !book.getTitle().equals(searchForm.getTitle())) {
                    result = false;
                } else if (searchForm.getGenre() != null && !book.getGenre().equals(searchForm.getGenre())) {
                    result = false;
                } else if (!searchForm.isShowUnavailable() && book.getAvailableCount() == 0) {
                    result = false;
                }
                return result;
            }).toList();
        });
    }

    @Override
    public List<Book> findBooksByUsername(@NotNull String username) {
        return transactionalOperation(conn -> {
            var books = new TreeMap<Long, Integer>();
            historyRepository.getUserHistory(conn, username).forEach(history -> {
                if (history.getOperationType().equals("get")) {
                    books.put(history.getBookId(), books.getOrDefault(history.getBookId(), 0) + 1);
                } else {
                    books.put(history.getBookId(), books.getOrDefault(history.getBookId(), 0) - 1);
                }
            });
            return booksRepository.findBooksByIds(conn, books.keySet().stream().filter(key -> books.get(key) > 0).toList());
        });
    }

    @Override
    public List<History> findHistoryByUsername(@NotNull String username) {
        return transactionalOperation(conn -> {
            return historyRepository.getUserHistory(conn, username);
        });
    }

    @Override
    public void getBooks(@NotNull String username, @NotNull List<Long> books) {
        transactionalOperation(conn -> {
            historyRepository.getBooks(conn, books.stream().map(bookId -> {
                var history = new History();
                history.setBookId(bookId);
                history.setUsername(username);
                history.setOperationType("get");
                return history;
            }).toList());
        });
    }

    @Override
    public void returnBooks(@NotNull String username, @NotNull List<Long> books) {
        transactionalOperation(conn -> {
            historyRepository.returnBooks(conn, books.stream().map(bookId -> {
                var history = new History();
                history.setBookId(bookId);
                history.setUsername(username);
                history.setOperationType("return");
                return history;
            }).toList());
        });
    }

    @Override
    public void createBook(@NotNull Book book) {
        transactionalOperation(conn -> {
            booksRepository.createBook(conn, book);
        });
    }

    @Override
    public void updateBook(@NotNull Book book) {
        transactionalOperation(conn -> {
            booksRepository.findBookById(conn, book.getId()).orElseThrow(BookNotFoundException::new);
            booksRepository.updateBook(conn, book);
        });
    }

    @Override
    public void deleteBook(@NotNull Long book) {
        transactionalOperation(conn -> {
            booksRepository.findBookById(conn, book).orElseThrow(BookNotFoundException::new);
            booksRepository.deleteBook(conn, book);
        });
    }

    private <T> T transactionalOperation(@NotNull DatabaseFunction<T> operation) {
        try (var connection = databaseManager.getConnection()) {
            try {
                var result = operation.apply(connection);
                connection.commit();
                return result;
            } catch (Error | Exception e) {
                connection.rollback();
                throw e;
            } finally {
                databaseManager.returnConnection(connection);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }

    private void transactionalOperation(@NotNull DatabaseConsumer operation) {
        try (var connection = databaseManager.getConnection()) {
            try {
                operation.accept(connection);
                connection.commit();
            } catch (Error | Exception e) {
                connection.rollback();
                throw e;
            } finally {
                databaseManager.returnConnection(connection);
            }
        } catch (SQLException e) {
            throw new DatabaseException(e);
        }
    }
}
