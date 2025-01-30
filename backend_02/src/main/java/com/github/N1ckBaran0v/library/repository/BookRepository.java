package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
}
