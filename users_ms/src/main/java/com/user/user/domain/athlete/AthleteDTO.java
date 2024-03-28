package com.user.user.domain.athlete;

import java.time.LocalDate;

public record AthleteDTO(String firstName,
        String lastName,
        LocalDate birthDate,
        String phone,
        String picture,
        String category,
        Boolean isStarting) {
}