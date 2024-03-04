package com.user.user.models.injury;

import java.time.LocalDate;

import com.user.user.models.athlete.Athlete;

public record InjuryDTO(String type, LocalDate inicialDate, LocalDate finalDate, Athlete athlete) {
}