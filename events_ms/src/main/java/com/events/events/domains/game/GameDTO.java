package com.events.events.domains.game;

import java.time.LocalDateTime;
import java.util.UUID;

public record GameDTO(
        LocalDateTime inicialDateTime,
        LocalDateTime finalDateTime,
        String local,
        UUID challenger,
        UUID challenged) {
}