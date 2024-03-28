package com.user.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domain.user.User;

public interface UserRepository extends JpaRepository<User, UUID > {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndPassword(String email, String password);
}