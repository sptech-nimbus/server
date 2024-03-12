package com.user.user.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.domains.user.User;

public interface UserRepository extends JpaRepository<User, UUID > {
    List<User> findByEmail(String email);

    User findByEmailAndPassword(String email, String password);
}