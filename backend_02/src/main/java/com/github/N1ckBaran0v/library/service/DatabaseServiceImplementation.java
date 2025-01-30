package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.repository.BookRepository;
import com.github.N1ckBaran0v.library.repository.HistoryRepository;
import com.github.N1ckBaran0v.library.repository.UserRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.TreeMap;
import java.util.stream.StreamSupport;

@Service
public class DatabaseServiceImplementation implements DatabaseService {
    private final BookRepository bookRepository;
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    @Autowired
    public DatabaseServiceImplementation(@NotNull BookRepository bookRepository,
                                         @NotNull HistoryRepository historyRepository,
                                         @NotNull UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User findUserByUsername(@NotNull String username) {
        return userRepository.findById(username).orElseThrow(UserNotFoundException::new);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteUserByUsername(@NotNull String username) {
        userRepository.findById(username).orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(username);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createUser(@NotNull User user) {
        userRepository.findById(user.getUsername()).ifPresent(u -> {
            throw new UserAlreadyExistsException();
        });
        userRepository.save(user);
    }

    @Override
    @Transactional
    public List<Book> findBooksByParams(@NotNull SearchForm searchForm) {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false).filter(book -> {
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
    }

    @Override
    @Transactional
    public List<Book> findBooksByUsername(@NotNull String username) {
        var books = new TreeMap<Long, Integer>();
        historyRepository.findAllByUsername(username).forEach(history -> {
            if (history.getOperationType().equals("get")) {
                books.put(history.getBookId(), books.getOrDefault(history.getBookId(), 0) + 1);
            } else {
                books.put(history.getBookId(), books.getOrDefault(history.getBookId(), 0) - 1);
            }
        });
        return StreamSupport.stream(bookRepository.findAllById(books.keySet().stream().filter(key -> books.get(key) > 0).toList()).spliterator(), false).toList();
    }

    @Override
    @Transactional
    public List<History> findHistoryByUsername(@NotNull String username) {
        return historyRepository.findAllByUsername(username);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void getBooks(@NotNull String username, @NotNull List<Long> books) {
        historyRepository.saveAll(books.stream().map(bookId -> {
            var history = new History();
            history.setBookId(bookId);
            history.setUsername(username);
            history.setOperationType("get");
            return history;
        }).toList());
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void returnBooks(@NotNull String username, @NotNull List<Long> books) {
        historyRepository.saveAll(books.stream().map(bookId -> {
            var history = new History();
            history.setBookId(bookId);
            history.setUsername(username);
            history.setOperationType("return");
            return history;
        }).toList());
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void createBook(@NotNull Book book) {
        bookRepository.save(book);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void updateBook(@NotNull Book book) {
        bookRepository.findById(book.getId()).orElseThrow(BookNotFoundException::new);
        bookRepository.save(book);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteBook(@NotNull Long book) {
        bookRepository.findById(book).orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(book);
    }
}
