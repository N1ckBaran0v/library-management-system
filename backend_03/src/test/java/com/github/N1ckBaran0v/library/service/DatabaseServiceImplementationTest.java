package com.github.N1ckBaran0v.library.service;

import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.data.History;
import com.github.N1ckBaran0v.library.data.User;
import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.repository.BookRepository;
import com.github.N1ckBaran0v.library.repository.HistoryRepository;
import com.github.N1ckBaran0v.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DatabaseServiceImplementationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private DatabaseServiceImplementation databaseService;

    private List<Book> books;

    @BeforeEach
    void setUp() {
        var first = new Book();
        first.setId(1L);
        first.setTitle("Title1");
        first.setAuthor("Author1");
        first.setGenre("Genre1");
        first.setAvailableCount(1);
        first.setTotalCount(1);
        var second = new Book();
        second.setId(2L);
        second.setTitle("Title2");
        second.setAuthor("Author1");
        second.setGenre("Genre2");
        second.setAvailableCount(1);
        second.setTotalCount(1);
        var third = new Book();
        third.setTitle("Title3");
        third.setAuthor("Author2");
        third.setGenre("Genre2");
        third.setAvailableCount(1);
        third.setTotalCount(1);
        var fourth = new Book();
        fourth.setTitle("Title3");
        fourth.setAuthor("Author3");
        fourth.setGenre("Genre3");
        fourth.setAvailableCount(1);
        fourth.setTotalCount(1);
        var fifth = new Book();
        fifth.setId(5L);
        fifth.setTitle("Title4");
        fifth.setAuthor("Author4");
        fifth.setGenre("Genre4");
        fifth.setAvailableCount(0);
        fifth.setTotalCount(1);
        books = List.of(first, second, third, fourth, fifth);
    }

    @Test
    void findUserByUsernameSuccess() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.of(user));
        assertEquals(user, databaseService.findUserByUsername("username"));
    }

    @Test
    void findUserByUsernameNotFound() {
        given(userRepository.findById("username")).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.findUserByUsername("username"));
    }

    @Test
    void deleteUserByUsernameSuccess() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.of(user));
        databaseService.deleteUserByUsername("username");
    }

    @Test
    void deleteUserByUsernameNotFound() {
        given(userRepository.findById("username")).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.deleteUserByUsername("username"));
    }

    @Test
    void createUserSuccess() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.empty());
        databaseService.createUser(user);
    }

    @Test
    void createUserAlreadyExists() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.of(user));
        assertThrows(UserAlreadyExistsException.class, () -> databaseService.createUser(user));
    }

    @Test
    void findBooksByParamsSuccess1() {
        var result = List.of(books.get(0), books.get(1), books.get(2), books.get(3));
        given(bookRepository.findAll()).willReturn(books);
        assertEquals(result, databaseService.findBooksByParams(new SearchForm()));
    }

    @Test
    void findBooksByParamsSuccess2() {
        var form = new SearchForm();
        form.setTitle("Title3");
        var result = List.of(books.get(2), books.get(3));
        given(bookRepository.findAll()).willReturn(books);
        assertEquals(result, databaseService.findBooksByParams(form));
    }

    @Test
    void findBooksByParamsSuccess3() {
        var form = new SearchForm();
        form.setAuthor("Author1");
        var result = List.of(books.get(0), books.get(1));
        given(bookRepository.findAll()).willReturn(books);
        assertEquals(result, databaseService.findBooksByParams(form));
    }

    @Test
    void findBooksByParamsSuccess4() {
        var form = new SearchForm();
        form.setGenre("Genre2");
        var result = List.of(books.get(1), books.get(2));
        given(bookRepository.findAll()).willReturn(books);
        assertEquals(result, databaseService.findBooksByParams(form));
    }

    @Test
    void findBooksByParamsSuccess5() {
        var form = new SearchForm();
        form.setShowUnavailable(true);
        given(bookRepository.findAll()).willReturn(books);
        assertEquals(books, databaseService.findBooksByParams(form));
    }

    @Test
    void findBooksByParamsSuccess6() {
        given(bookRepository.findAll()).willReturn(List.of());
        assertEquals(List.of(), databaseService.findBooksByParams(new SearchForm()));
    }

    @Test
    void findBooksByUsernameSuccess() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.of(user));
        var first = new History();
        first.setBookId(1L);
        first.setOperationType("get");
        var second = new History();
        second.setBookId(2L);
        second.setOperationType("get");
        var third = new History();
        third.setBookId(2L);
        third.setOperationType("return");
        var history = List.of(first, second, third);
        given(historyRepository.findAllByUsername("username")).willReturn(history);
        given(bookRepository.findAllById(List.of(1L))).willReturn(List.of(books.getFirst()));
        assertEquals(List.of(books.getFirst()), databaseService.findBooksByUsername("username"));
    }

    @Test
    void findBooksByUsernameNotFound() {
        given(userRepository.findById("username")).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.findBooksByUsername("username"));
        verify(historyRepository, never()).findAllByUsername(any());
        verify(bookRepository, never()).findAllById(any());
    }

    @Test
    void findHistoryByUsernameSuccess() {
        var user = new User();
        user.setUsername("username");
        given(userRepository.findById("username")).willReturn(Optional.of(user));
        var first = new History();
        first.setBookId(1L);
        first.setOperationType("get");
        var second = new History();
        second.setBookId(2L);
        second.setOperationType("get");
        var third = new History();
        third.setBookId(2L);
        third.setOperationType("return");
        var history = List.of(first, second, third);
        given(historyRepository.findAllByUsername("username")).willReturn(history);
        assertEquals(history, databaseService.findHistoryByUsername("username"));
    }

    @Test
    void findHistoryByUsernameNotFound() {
        given(userRepository.findById("username")).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.findHistoryByUsername("username"));
        verify(historyRepository, never()).findAllByUsername(any());
    }

    @Test
    void getBooksSuccess() {
        var list = List.of(1L, 2L);
        given(userRepository.findById("username")).willReturn(Optional.of(new User()));
        given(bookRepository.findAllById(list)).willReturn(List.of(books.get(0), books.get(1)));
        databaseService.getBooks("username", list);
        verify(bookRepository).saveAll(any());
        verify(historyRepository).saveAll(any());
    }

    @Test
    void getBooksNotFound1() {
        var list = List.of(1L, 1L);
        given(userRepository.findById("username")).willReturn(Optional.of(new User()));
        given(bookRepository.findAllById(list)).willReturn(List.of(books.getFirst()));
        assertThrows(BookNotFoundException.class, () -> databaseService.getBooks("username", list));
        verify(bookRepository, never()).saveAll(any());
        verify(historyRepository, never()).saveAll(any());
    }

    @Test
    void getBooksNotFound2() {
        var list = List.of(1L, 2L);
        given(userRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.getBooks("username", list));
        verify(bookRepository, never()).findAllById(any());
        verify(bookRepository, never()).saveAll(any());
        verify(historyRepository, never()).saveAll(any());
    }

    @Test
    void returnBooksSuccess() {
        var list = List.of(5L);
        given(userRepository.findById("username")).willReturn(Optional.of(new User()));
        given(bookRepository.findAllById(list)).willReturn(List.of(books.getLast()));
        databaseService.returnBooks("username", list);
        verify(bookRepository).saveAll(any());
        verify(historyRepository).saveAll(any());
    }

    @Test
    void returnBooksNotFound1() {
        var list = List.of(5L, 5L);
        given(userRepository.findById("username")).willReturn(Optional.of(new User()));
        given(bookRepository.findAllById(list)).willReturn(List.of(books.getLast()));
        assertThrows(BookNotFoundException.class, () -> databaseService.returnBooks("username", list));
        verify(bookRepository, never()).saveAll(any());
        verify(historyRepository, never()).saveAll(any());
    }

    @Test
    void returnBooksNotFound2() {
        var list = List.of(5L);
        given(userRepository.findById(any())).willReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> databaseService.returnBooks("username", list));
        verify(bookRepository, never()).findAllById(any());
        verify(bookRepository, never()).saveAll(any());
        verify(historyRepository, never()).saveAll(any());
    }

    @Test
    void createBookSuccess() {
        var book = new Book();
        book.setAuthor("Author5");
        book.setGenre("Genre5");
        book.setTitle("Title5");
        book.setAvailableCount(5);
        book.setTotalCount(5);
        databaseService.createBook(book);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBookSuccess() {
        var book = new Book();
        book.setId(2L);
        book.setTitle("Title");
        book.setAuthor("Author");
        book.setGenre("Genre");
        book.setAvailableCount(5);
        book.setTotalCount(5);
        given(bookRepository.findById(book.getId())).willReturn(Optional.of(books.get(1)));
        databaseService.updateBook(book);
        verify(bookRepository).save(book);
    }

    @Test
    void updateBookNotFound() {
        var book = new Book();
        book.setId(6L);
        given(bookRepository.findById(book.getId())).willReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> databaseService.updateBook(book));
        verify(bookRepository, never()).save(book);
    }

    @Test
    void deleteBookSuccess() {
        given(bookRepository.findById(1L)).willReturn(Optional.of(books.getFirst()));
        databaseService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBookNotFound() {
        given(bookRepository.findById(1L)).willReturn(Optional.empty());
        assertThrows(BookNotFoundException.class, () -> databaseService.deleteBook(1L));
        verify(bookRepository, never()).deleteById(1L);
    }
}