package com.user.user.domains.coach;

import java.time.LocalDate;

public record CoachDTO(
        String firstName, String lastName,
        LocalDate birthDate,
        String phone, String picture) {

}
