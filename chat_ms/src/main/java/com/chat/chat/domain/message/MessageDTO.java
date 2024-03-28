package com.chat.chat.domain.message;

import java.util.UUID;

public record MessageDTO(String message, UUID userId) {
}