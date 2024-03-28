package com.events.events.domain.training;

import java.time.LocalDateTime;
import java.util.UUID;

public record TrainingDTO(LocalDateTime inicialDateTime,
                LocalDateTime finalDateTime,
                String local,
                UUID teamId) {
}