package com.user.user.domain.user;

import java.time.LocalDateTime;

public record ChangePasswordRequestDTO(String email, LocalDateTime expirationDate) {
    
}