package com.user.user.domains.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChangePasswordRequestDTO(UUID id, LocalDateTime expirationDate) {
    
}
