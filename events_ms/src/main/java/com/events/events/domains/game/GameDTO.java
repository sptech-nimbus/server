package com.events.events.domains.game;

import java.time.LocalDateTime;

public record GameDTO(
                LocalDateTime dateTime,
                String local,
                String challenger,
                String challenged) {
}