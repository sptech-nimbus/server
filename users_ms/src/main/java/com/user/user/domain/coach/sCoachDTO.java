package com.user.user.domain.coach;

import java.time.LocalDate;
import java.util.List;

import com.user.user.domain.team.Team;

public record sCoachDTO(String firstName,
        String lastName,
        LocalDate birthDate,
        List<Team> teams) {
    public sCoachDTO(Coach coach) {
        this(coach.getFirstName(), coach.getLastName(), coach.getBirthDate(), coach.getTeams());
    }
}
