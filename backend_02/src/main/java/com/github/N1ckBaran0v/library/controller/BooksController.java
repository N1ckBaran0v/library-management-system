package com.github.N1ckBaran0v.library.controller;

import com.github.N1ckBaran0v.library.component.SessionInfo;
import com.github.N1ckBaran0v.library.data.Book;
import com.github.N1ckBaran0v.library.form.SearchForm;
import com.github.N1ckBaran0v.library.service.BookService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BooksController {
    private final BookService bookService;

    @Autowired
    public BooksController(@NotNull BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/find")
    public ResponseEntity<?> find(@RequestBody SearchForm searchForm, @NotNull SessionInfo sessionInfo) {
        return new ResponseEntity<>(bookService.findBooks(sessionInfo, searchForm), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Book book, @NotNull SessionInfo sessionInfo) {
        bookService.createBook(sessionInfo, book);
        return new ResponseEntity<>("201 Created", HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody Book book, @NotNull SessionInfo sessionInfo) {
        bookService.updateBook(sessionInfo, book);
        return new ResponseEntity<>("202 Accepted", HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Long book, @NotNull SessionInfo sessionInfo) {
        bookService.deleteBook(sessionInfo, book);
        return new ResponseEntity<>("204 No Content", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/get")
    public ResponseEntity<?> getBooks(@RequestBody List<Long> books,
                                      @RequestParam String username,
                                      @NotNull SessionInfo sessionInfo) {
        bookService.getBooks(sessionInfo, username, books);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

    @PostMapping("/return")
    public ResponseEntity<?> returnBooks(@RequestBody List<Long> books,
                                         @RequestParam String username,
                                         @NotNull SessionInfo sessionInfo) {
        bookService.returnBooks(sessionInfo, username, books);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
