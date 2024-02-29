package com.user.user.models.coach;

import java.time.LocalDate;

public record CoachDTO(
        String firstName, String lastName,
        LocalDate birthDate,
        String phone, Long picture, String userId) {

}
