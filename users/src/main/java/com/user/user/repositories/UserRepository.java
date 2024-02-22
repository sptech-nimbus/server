package com.user.user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.user.user.models.user.User;

public interface UserRepository extends JpaRepository<User, String> {
    
}
