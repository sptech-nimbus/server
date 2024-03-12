package com.chat.chat.domains.message;

import java.util.UUID;

public record MessageDTO(String message, UUID userId) {
}