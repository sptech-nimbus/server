package com.user.user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.user.user.models.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByEmail(String email);

    @Query(value = "SELECT * FROM User u WHERE BINARY u.email = ?1 AND BINARY u.password = ?2", nativeQuery = true)
    User findByEmailAndPassword(String email, String password);
}