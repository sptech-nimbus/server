package com.user.user.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChangePasswordRequestDTO(UUID id, LocalDateTime expirationDate) {
    
}
