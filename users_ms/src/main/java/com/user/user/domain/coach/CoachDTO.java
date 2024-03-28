package com.user.user.domain.coach;

import java.time.LocalDate;

public record CoachDTO(
        String firstName, String lastName,
        LocalDate birthDate,
        String phone, String picture) {

}
