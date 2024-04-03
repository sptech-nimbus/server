package com.events.events.domain.game;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameDTO(
                LocalDateTime inicialDateTime,
                LocalDateTime finalDateTime,
                String local,
                UUID challenger,
                UUID challenged) {
}