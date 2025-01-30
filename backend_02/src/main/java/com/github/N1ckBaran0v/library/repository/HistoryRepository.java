package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.History;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Long> {
    List<History> findAllByUsername(@NotNull String username);
}
