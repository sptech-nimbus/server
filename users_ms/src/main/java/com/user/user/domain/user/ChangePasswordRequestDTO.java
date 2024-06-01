package com.user.user.domain.user;

public record ChangePasswordRequestDTO(String email, Long expirationDate) {
    
}