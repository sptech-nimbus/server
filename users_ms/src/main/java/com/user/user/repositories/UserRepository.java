package com.user.user.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.models.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByEmail(String email);

    User findByEmailAndPassword(String email, String password);

    User findByUserId(String userId);
}