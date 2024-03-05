package com.user.user.domains.injury;

import java.time.LocalDate;

import com.user.user.domains.athlete.Athlete;

public record InjuryDTO(String type, LocalDate inicialDate, LocalDate finalDate, Athlete athlete) {
}