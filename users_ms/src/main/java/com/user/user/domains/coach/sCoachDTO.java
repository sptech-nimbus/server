package com.user.user.domains.coach;

import java.time.LocalDate;
import java.util.List;

import com.user.user.domains.team.Team;

public record sCoachDTO(String firstName,
        String lastName,
        LocalDate birthDate,
        List<Team> teams) {
    public sCoachDTO(Coach coach) {
        this(coach.getFirstName(), coach.getLastName(), coach.getBirthDate(), coach.getTeams());
    }
}
