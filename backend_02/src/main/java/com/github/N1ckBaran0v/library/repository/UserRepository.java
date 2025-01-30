package com.github.N1ckBaran0v.library.repository;

import com.github.N1ckBaran0v.library.data.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
}
