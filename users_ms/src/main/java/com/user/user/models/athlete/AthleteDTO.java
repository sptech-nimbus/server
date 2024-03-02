package com.user.user.models.athlete;

import java.time.LocalDate;

import com.user.user.models.team.Team;

public record AthleteDTO(String firstName,
        String lastName,
        LocalDate birthDate,
        String phone,
        String picture,
        String category,
        Boolean isStarting,
        Team team) {

}
