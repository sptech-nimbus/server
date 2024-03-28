package com.user.user.domain.injury;

import java.time.LocalDate;

import com.user.user.domain.athlete.Athlete;

public record InjuryDTO(String type, LocalDate inicialDate, LocalDate finalDate, Athlete athlete) {
}